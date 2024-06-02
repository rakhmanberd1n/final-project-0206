package nazym.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USERS")
@Builder
@ToString
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID",nullable = false)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="SURNAME")
    private String surname;

    @Column(name="EMAIL")
    private String email;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="PICTURE")
    private String picture;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    private List<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return roles;
    }

    @Override
    public String getUsername()
    {
        return email;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}