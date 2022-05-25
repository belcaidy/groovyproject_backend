package api

import grails.converters.JSON
import grails.converters.XML
import project.Annonce

class ApiController {

    def annonce() {
        switch (request.getMethod()) {
            case "GET":
                if (!params.id)
                    return response.status = 400
                // L'id a bien été précisé lors de l'appel
                def annonceInstance = Annonce.get(params.id)
                if (!annonceInstance)
                    return response.status = 404
                // On a trouvé la ressource, on peut la renvoyer
                renderThis(annonceInstance, request.getHeader('Accept'))
//                response.withFormat {
//                    xml { render annonceInstance as XML }
//                    json { render annonceInstance as JSON }
//                }
                break
            case "PUT":
                break
            case "PATCH":
                break
            case "DELETE":
                break
            default:
                return response.status = 405
                break
        }
        return response.status = 406
    }

    def renderThis(Object instance, String accept )
    {
        switch (accept)
        {
            case "json":
            case "application/json":
            case "text/json":
                render instance as JSON
                break;
            case "xml":
            case "application/xml":
            case "text/xml":
                render instance as XML
                break;
        }
    }
}
