package ru.kirillov.springboot.task311.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillov.springboot.task311.dao.RoleDao;
import ru.kirillov.springboot.task311.models.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String role){
        return roleDao.getRoleByName(role);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleDao.saveRole(role);
    }

    @Override
    @Transactional
    public Set<Role> getSetRoleFromArray(String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : roles) {
            rolesSet.add(roleDao.getRoleByName(role));
        }
        return rolesSet;
    }

    @Override
    @Transactional
    public Set<Role> checkRoles(String[] strRoles, String newRole) {
        Set<Role> roles = new HashSet<>();
        if (strRoles != null) {
            roles = getSetRoleFromArray(strRoles);
        }
        if (!newRole.equals("")) {                                     // Если есть новая роль - сохраняем
            Role role = new Role(newRole);
            saveRole(role);
            roles.add(role);
        }
        return roles;
    }
}
