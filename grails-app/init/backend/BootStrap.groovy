package backend

import project.*

class BootStrap {

    def init = { servletContext ->

//        def userSuperAdmin = new User(username: 'superadmin', password: 'superadmin').save()
        def userAdmin = new User(username: 'admin', password: 'admin').save()
//        def userMod = new User(username: 'modo', password: 'modo').save()
        def customer = new User(username: 'customer', password: 'customer').save()

//        def roleSuperAdmin = new Role(authority: 'SUPER_ADMIN').save()
        def roleAdmin = new Role(authority: 'ROLE_ADMIN').save()
//        def roleMod = new Role(authority: 'ROLE_MODO').save()
        def roleCustomer = new Role(authority: 'ROLE_CUSTOMER').save()

//        UserRole.create(userSuperAdmin, roleSuperAdmin, true)
        UserRole.create(userAdmin, roleAdmin, true)
//        UserRole.create(userMod, roleMod, true)
        UserRole.create(customer, roleCustomer, true)

    /*    ["Alice", "Bob", "Charly", "David", "Etienne"].each {
            String name ->
                def userInstance = new User(username: name, password: "password").save()
                (1..5).each {
                    Integer annonceIdx ->
                        def annonceInstance =
                                new Annonce(
                                        title: "Annonce $name $annonceIdx",
                                        description: "Description $name $annonceIdx",
                                        price: annonceIdx * 100,
                                        status: Boolean.TRUE,
                                )
                       *//* (1..3).each {
                            annonceInstance.addToIllustrations(new Illustration(fileBytes: "grails.svg"))
                        }*//*
                        userInstance.addToAnnonces(annonceInstance)
                }
                userInstance.save(flush: true, failOnError: true)
        }*/

    }
    def destroy = {
    }
}
