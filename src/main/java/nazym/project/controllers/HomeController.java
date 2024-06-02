package nazym.project.controllers;

import jakarta.servlet.http.HttpSession;
import nazym.project.models.BasketItem;
import nazym.project.models.Order;
import nazym.project.models.Product;
import nazym.project.models.User;
import nazym.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class HomeController
{
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PictureController pictureController;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private EmailSenderService emailSenderService;

    private User recovery_user;

    private String code;

    @GetMapping("/")
    public String home()
    {
        List<BasketItem> basket = (List<BasketItem>) httpSession.getAttribute("basket");
        if(basket == null)
        {
            httpSession.setAttribute("basket",new ArrayList<BasketItem>());
        }
        return "home";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/sign-in")
    public String signInPage()
    {
        return "sign-in";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public String registerPage()
    {
        return "register";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public String register(User user, @RequestParam(name = "rePassword") String rePassword)
    {
        String result = userService.addUser(user, rePassword);
        return "redirect:/"+result;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profilePage()
    {
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/basket")
    public String basketPage(Model model)
    {
        List<BasketItem> basket = (List<BasketItem>) httpSession.getAttribute("basket");
        double total = 0;
        for(BasketItem basketItem : basket)
        {
            total += basketItem.getProduct().getPrice()*basketItem.getQuantity();
        }
        model.addAttribute("basket", basket);
        model.addAttribute("total", total);
        return "basket";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myOrders")
    public String myOrders(Model model)
    {
        List<Order> orders = orderService.findUserOrders(userService.getCurrentUser().getId());
        model.addAttribute("orders", orders);
        return "myOrders";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String renewPassword)
    {

        String result = userService.updatePassword(currentPassword, newPassword, renewPassword);
        return "redirect:/"+result;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateDetails")
    public String updateDetails(@RequestParam String name,
                                @RequestParam String surname)
    {
        userService.updateDetails(name, surname);
        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/setImage")
    public String setImage(@RequestPart(name = "file") MultipartFile multipartFile) throws IOException
    {
        userService.setImage(multipartFile);
        pictureController.addPictureLocal(multipartFile);
        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/resetImage")
    public String resetImage()
    {
        userService.resetImage();
        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String deletion)
    {
        if(!deletion.equals("DELETION")) return "redirect:/profile";
        userService.deleteAccount();
        return "redirect:/logout";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPanel()
    {
        return "admin_panel";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/category")
    public String adminCategory(Model model)
    {
        model.addAttribute("categories", categoryService.allCategories());
        return "admin_category";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/product")
    public String adminProduct(Model model)
    {
        model.addAttribute("products", productService.allProducts());
        model.addAttribute("categories", categoryService.allCategories());
        model.addAttribute("features", featureService.allFeatures());
        return "admin_product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/feature")
    public String adminFeature(Model model)
    {
        model.addAttribute("features", featureService.allFeatures());
        return "admin_feature";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public String adminUsers()
    {
        return "admin_user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/comments")
    public String adminComments()
    {
        return "admin_comment";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/orders")
    public String adminOrders()
    {
        return "admin_order";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/removeFeature/{id}")
    public String removeFeature(@PathVariable(name = "id") Long id,
                                @RequestParam(name = "feature") Long feature)
    {
        Product product = productService.findProduct(id);
        product.getFeatures().remove(featureService.findFeature(feature));
        productService.updateProduct(product);
        return "redirect:/admin/product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addFeature/{id}")
    public String addFeature(@PathVariable(name = "id") Long id,
                             @RequestParam(name = "feature") Long feature)
    {
        Product product = productService.findProduct(id);
        product.getFeatures().add(featureService.findFeature(feature));
        productService.updateProduct(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/categories")
    public String allCategories(Model model)
    {
        model.addAttribute("categories", categoryService.allCategories());
        return "client_category";
    }

    @GetMapping("/category/{id}")
    public String categoryDetails(@PathVariable(name = "id") Long id, Model model)
    {
        model.addAttribute("category", categoryService.findCategory(id));
        return "client_category_details";
    }
    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable(name = "id") Long id,
                                 Model model)
    {
        boolean contain = false;
        List<BasketItem> list = (List<BasketItem>) httpSession.getAttribute("basket");
        if(list != null)
        {
            List<Product> products = new ArrayList<>();

            for(BasketItem b : list) products.add(b.getProduct());

            for(Product p : products)
            {
                if(p.getId().equals(id))
                {
                    contain = true;
                    break;
                }
            }
        }
        Product product = productService.findProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("contains", contain);
        return "client_product_details";
    }

    @PostMapping("/search")
    public String searchProducts(@RequestParam String pattern,
                                 Model model)
    {
        List<Product> searchProducts = productService.searchProducts(pattern);
        model.addAttribute("products",searchProducts);
        return "search";
    }


    @GetMapping("/forgot")
    @PreAuthorize("isAnonymous()")
    public String forgotPassword()
    {
        return "forgot";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/send")
    public String sendRecovery(@RequestParam String user_email)
    {
        recovery_user = (User) userService.loadUserByUsername(user_email);
        if(recovery_user != null)
        {
            Random random = new Random();
            code = "Your recovery code is : \n"+(random.nextInt(999999 - 100000) + 1000000);
            emailSenderService.sendEmail(user_email,"Recover password", code);
            return "redirect:/forgot?sent";
        }
        return "redirect:/forgot?mail&no";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/checkRecover")
    public String checkRecovery(@RequestParam String user_code)
    {
        return code.equals(user_code) ? "redirect:/forgot?success" : "redirect:/forgot?sent&wrong";
    }
    @PreAuthorize("isAnonymous()")
    @PostMapping("/recoverPassword")
    public String recoverPassword(@RequestParam String newPassword,
                                  @RequestParam String renewPassword)
    {
        if(!newPassword.equals(renewPassword)) return "redirect:/forgot?success&differ";
        else
        {
            userService.recoverPassword(recovery_user,newPassword);
            return "redirect:/sign-in?updated";
        }
    }
}