package ru.kirillov.springboot.task311.services;

import ru.kirillov.springboot.task311.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String role);
    void saveRole(Role role);
    Set<Role> getSetRoleFromArray(String[] roles);

    Set<Role> checkRoles(String[] roles, String newRole);
}
