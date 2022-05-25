package api

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import project.Role
import project.User
import project.UserRole
import project.UserService

@Secured('permitAll')
class UserController {

    UserService userService

    def list() {
        def userList = User.findAll()
        List list = new ArrayList<>();
        userList.each { it ->
            Map map = [:]
            map.put("id", it.id)
            map.put("username", it.username)
            map.put("status", it.enabled)
            map.put("role", UserRole.findByUser(it).role.authority)
            list.add(map)
        }

        render list as JSON
    }

    //User creating by admin
    def registration(UserDto userDto) {
        User adminUser = new User(username: userDto.username, password: userDto.password)
        def user, result
        try {
            user = userService.save(adminUser)
            def adminRole = Role.findByAuthority(userDto.role)
            UserRole.create(user, adminRole, true)
            result = [
                    admin: adminUser.username, role: adminRole.authority
            ]
            render result as JSON
        }
        catch (ValidationException e) {
            def data = [res: 'Username Must be Unique']
            render(status: 400, text: (data as JSON).toString(), contentType: 'application/json')
        }
    }

//    Customer registration from public user
    def customerRegistration(UserDto userDto) {
        User adminUser = new User(username: userDto.username, password: userDto.password)
        def user, result
        try {
            user = userService.save(adminUser)
            def adminRole = Role.findByAuthority("ROLE_CUSTOMER")
            UserRole.create(user, adminRole, true)
            result = [
                    admin: adminUser.username, role: adminRole.authority
            ]
            render result as JSON
        }
        catch (ValidationException e) {
            def data = [res: 'Username Must be Unique']
            render(status: 400, text: (data as JSON).toString(), contentType: 'application/json')
        }
    }

    def getUser(Long id) {
        def user = User.findById(id)
        def userRole = UserRole.findByUser(user)
        def dto = new UserDto()
        dto.username = userRole.user.getUsername()
        dto.password = userRole.user.getPassword()
        dto.role = userRole.role.getAuthority()
        render dto as JSON
    }

    def updateByCustomer(UserDto userDto) {
        def getUser = User.findByUsername(userDto.username)
        User adminUser = new User(id: getUser.id, username: userDto.username, password: userDto.password)
        def user, result
        try {
            user = userService.save(adminUser)
            def adminRole = Role.findByAuthority("ROLE_CUSTOMER")
            UserRole.create(user, adminRole, true)

            result = [
                    admin: adminUser.username, role: adminRole.authority
            ]
            render result as JSON
        }
        catch (ValidationException e) {
            def data = [res: 'Username Must be Unique']
            render(status: 400, text: (data as JSON).toString(), contentType: 'application/json')
        }
    }

    def updateByAdmin(UserDto userDto) {
        def getUser = User.findByUsername(userDto.username)
        User adminUser = new User(id: getUser.id, username: userDto.username, password: userDto.password)
        def user, result
        try {
            user = userService.save(adminUser)
            def adminRole = Role.findByAuthority(userDto.role)
            UserRole.create(user, adminRole, true)

            result = [
                    admin: adminUser.username, role: adminRole.authority
            ]
            render result as JSON
        }
        catch (ValidationException e) {
            def data = [res: 'Username Must be Unique']
            render(status: 400, text: (data as JSON).toString(), contentType: 'application/json')
        }
    }

    def deleteUserBySuperAdmin(Long id) {
        def user = User.findById(id)
        if (user) {
            def userRole = UserRole.findByUser(user)
            if (userRole.role.authority == "ROLE_ADMIN") {
                def data = [res: 'Admin can not be deleted!']
                render(status: 400, text: (data as JSON).toString(), contentType: 'application/json')
            } else {
                userRole.delete(flash: true)
                userService.delete(id)
                def data = [res: 'deleted!!']
                render data as JSON
            }
        } else {
            def data = [res: 'User Not found']
            render(status: 400, text: (data as JSON).toString(), contentType: 'application/json')
        }
    }
}

class UserDto {
    public Long id
    public String username
    public String password;
    public String role
}