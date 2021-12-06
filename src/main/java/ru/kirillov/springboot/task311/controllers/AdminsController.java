package ru.kirillov.springboot.task311.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kirillov.springboot.task311.models.Role;
import ru.kirillov.springboot.task311.models.User;
import ru.kirillov.springboot.task311.services.RoleService;
import ru.kirillov.springboot.task311.services.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admins")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // index-страница после редиректа
    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/getAllUsers";
    }

    // введенный в адресной строке id попадет с помощью @PathVariable в int id
    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "admin/getUser";
    }

    // GET-запрос создаст модель "newUser" и поместит его как объект для создания в new.html
    @GetMapping("/new")
    public String createUser(@ModelAttribute("newUser") User user, Model model) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/new";
    }

    // POST-метод возьмет "newUser" со страницы new.html,
    //  передаст его с помощью @ModelAttribute("newUser") в User user,
    //   передаст с помощью checkbox, "newRoles?ROLE_ADMIN" и @RequestParam в String[] strRoles - строковые роли
    //    и сделает .saveUser().
    // Если никакого User модель содержать не будет, то в User user поместится user с полями по умолчанию (0, null, null)
    @PostMapping()
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam String[] strRoles, String newRole) {

        Set<Role> roles = roleService.checkRoles(strRoles, newRole);

//        Set<Role> roles = new HashSet<>();
//        if (strRoles != null) {
//            roles = roleService.getSetRoleFromArray(strRoles);
//        }
//        if (!newRole.equals("")) {                                     // Если есть новая роль - сохраняем
//            Role role = new Role(newRole);
//            roleService.saveRole(role);
//            roles.add(role);
//        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Шифруем пароль
        userService.saveUser(user);
        return "redirect:/admins";
    }

    // GET-запрос со страницы getUser передаст id и перейдет в этот метод по адресу /{id}/edit
    // модель примет пользователя (+ его роли), найденного по id и откроет страницу editUser.html
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("existingUser", userService.getUser(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/editUser";
    }

    // PATCH-запрос из editUser.html возьмет пользователя из модели existingUser, поместит его в user,
    //  id принимается из editUser.html с помощью @PathVariable.
    //   Далее произойдет изменение с помощью updateUser()
    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("existingUser") User user,
                             @PathVariable("id") int id,
                             @RequestParam String[] strRoles, String newRole) {

        Set<Role> roles = roleService.checkRoles(strRoles, newRole);
        user.setRoles(roles);
        userService.updateUser(id, user);
        return "redirect:/admins";
    }

    // DELETE-запрос с id найдет пользователя, удалит его, и перейдет в admins/
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admins";
    }

//  Ручное добавление пользователей и ролей
//    @PostConstruct
//    public void myinit() {
//        Role role1 = new Role("ROLE_ADMIN");
//        Role role2 = new Role("ROLE_USER");
//        roleService.saveRole(role1);
//        roleService.saveRole(role2);
//        Set<Role> roles = new HashSet<>();
//        roles.add(role1);
//        roles.add(role2);
//
//        User user = new User();
//        user.setUsername("admin");
//        user.setPassword(passwordEncoder.encode("admin"));
//        user.setRoles(roles);
//
//        userService.saveUser(user);
//    }
}
