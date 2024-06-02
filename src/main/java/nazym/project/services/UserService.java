package nazym.project.services;

import jakarta.servlet.http.HttpSession;
import nazym.project.models.BasketItem;
import nazym.project.models.Product;
import nazym.project.models.Role;
import nazym.project.models.User;
import nazym.project.repositories.RoleRepository;
import nazym.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class UserService implements UserDetailsService
{

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private HttpSession httpSession;

    public List<User> allUsers()
    {
        return userRepository.findAll();
    }

    public void deleteUser(Long id)
    {
        userRepository.deleteById(id);
    }

    public void updateUser(User user)
    {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
    {
        return userRepository.findByEmail(email);
    }

    public String addUser(User user, String rePassword)
    {
        if(userRepository.findByEmail(user.getEmail()) != null) return "register?exist";
        if(!user.getPassword().equals(rePassword)) return "register?typo";
        user.setPassword(passwordEncoder.encode(rePassword));
        Role role = roleRepository.findRoleUser();
        user.setRoles(List.of(role));
        userRepository.save(user);
        return "sign-in?success";
    }

    public User getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) return null;
        return (User) authentication.getPrincipal();
    }

    public String updatePassword(String currentPassword, String newPassword, String renewPassword)
    {
        User user = getCurrentUser();
        if(passwordEncoder.matches(user.getPassword(), newPassword)) return "profile?same";
        if(!passwordEncoder.matches(currentPassword, user.getPassword())) return "profile?notright";
        if(!newPassword.equals(renewPassword)) return "profile?notmatch";
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "profile?success";
    }

    public void recoverPassword(User recovery_user, String newPassword)
    {
        recovery_user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(recovery_user);
    }

    public void updateDetails(String name, String surname)
    {
        User user = getCurrentUser();
        user.setName(name);
        user.setSurname(surname);
        userRepository.save(user);
    }

    public void setImage(MultipartFile multipartFile)
    {
        User user = getCurrentUser();
        user.setPicture(multipartFile.getOriginalFilename());
        userRepository.save(user);
    }

    public void deleteAccount()
    {
        User user = getCurrentUser();
        userRepository.delete(user);
    }

    public void resetImage()
    {
        User user = getCurrentUser();
        user.setPicture("anonymous.jpeg");
        userRepository.save(user);
    }

    public String addToBasketFirst(Long id)
    {
        Product product = productService.findProduct(id);
        if(product.getQuantity() <= 0) return "error";
        List<BasketItem> basket = (List<BasketItem>) httpSession.getAttribute("basket");
        BasketItem basketItem = BasketItem.builder()
                .product(product)
                .quantity(1)
                .build();
        basket.add(basketItem);
        httpSession.setAttribute("basket", basket);
        return "success";
    }


    public void addToBasket(int id)
    {
        List<BasketItem> basket = (List<BasketItem>) httpSession.getAttribute("basket");
        BasketItem basketItem = basket.get(id);
        if(basketItem.getProduct().getQuantity() >= basketItem.getQuantity()+1)
        {
            basketItem.setQuantity(basketItem.getQuantity()+1);
            basket.set(id,basketItem);
        }
        httpSession.setAttribute("basket",basket);
    }


    public void removeFromBasket(int id)
    {
        List<BasketItem> basket = (List<BasketItem>) httpSession.getAttribute("basket");
        BasketItem basketItem = basket.get(id);
        if(basketItem.getQuantity()==1)
        {
            deleteFromBasket(id);
        }
        else if (basketItem.getQuantity() > 1)
        {
            basketItem.setQuantity(basketItem.getQuantity()-1);
            basket.set(id,basketItem);
        }
        httpSession.setAttribute("basket",basket);
    }

    public void deleteFromBasket(int id)
    {
        List<BasketItem> basket = (List<BasketItem>) httpSession.getAttribute("basket");
        basket.remove(id);
        httpSession.setAttribute("basket",basket);
    }
}