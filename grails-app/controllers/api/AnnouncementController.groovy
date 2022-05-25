package api

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.validation.ValidationException
import project.Annonce
import project.AnnonceService
import project.Illustration
import project.User

@Secured('permitAll')
class AnnouncementController {

    AnnonceService annonceService
    SpringSecurityService springSecurityService


    def index() {
        def result = Annonce.list()
        List list = new ArrayList<>();
        result.each { it ->
            def map = [:]
            map.put("id", it.id)
            map.put('title', it.title)
            map.put('dateCreated', it.dateCreated)
            map.put('price', it.price)
            map.put('description', it.description)
            map.put('subTitle', it.subTitle)
            map.put('longDescription', "aaaaaaaaaa")
            map.put('lastUpdated', it.lastUpdated)
            list.add(map)
        }

        render list as JSON
    }


    def customerDashboard() {
        def result = Annonce.list()
        List list = new ArrayList<>();
        result.each { it ->
            def map = [:]
            map.put("id", it.id)
            map.put('title', it.title)
            map.put('dateCreated', it.dateCreated)
            map.put('price', it.price)
            map.put('description', it.description)
            map.put('subTitle', it.subTitle)
            map.put('lastUpdated', it.lastUpdated)
            def images = Illustration.findAllByAnnonce(it)
            map.put('images', images)
            list.add(map)
        }

        render list as JSON
    }


    def customerAnnouncement(String id) {
        List list = new ArrayList<>();
        def user = User.findByUsername(id);
        def result = Annonce.findAllByAuthor(user)
        result.each { it ->
            def map = [:]
            map.put("id", it.id)
            map.put('title', it.title)
            map.put('dateCreated', it.dateCreated)
            map.put('price', it.price)
            map.put('description', it.description)
            map.put('subTitle', it.subTitle)
            map.put('lastUpdated', it.lastUpdated)
            def images = Illustration.findAllByAnnonce(it)
            map.put('images', images)
            list.add(map)
        }

        render list as JSON
    }

    def get(Long id) {
        def result = [:]
        def announce = annonceService.get(id)
        def images = Illustration.findAllByAnnonce(announce)
        result.announce = announce
        result.images = images
        render result as JSON
    }

    @Transactional
    def create(AnnounceDto annunceDto) {
        def annonce = new Annonce()
        annonce.title = annunceDto.title
        annonce.description = annunceDto.description
        annonce.price = annunceDto.price
        annonce.subTitle = annunceDto.subTitle
        annunceDto.illustrations.each { ita ->
            def illustration = new Illustration()
            illustration.annonce = annonce;
            illustration.imageString = ita
            annonce.addIllustrations(illustration)
        }

        Long id = loggedUserId().toLong()
        annonce.author = User.findById(id)
        def save = annonceService.save(annonce)
        return save as JSON
    }

    @Transactional
    def update(AnnounceDto announceDto) {
        def announce = Annonce.findById(announceDto.id)
        announce?.title = announceDto.title
        announce?.description = announceDto.description
        announce?.price = announceDto.price
        announceDto?.illustrations?.each { ita ->
            def illustration = new Illustration()
            illustration.annonce = announce;
            illustration.imageString = ita
            announce.addIllustrations(illustration)
        }
        Long id = loggedUserId().toLong()
        announce.author = User.findById(id)
        def save = annonceService.save(announce)
        return save as JSON
    }

    @Transactional
    def delete(Long id) {
        annonceService.delete(id)
        def result = [message: "Successfully Delete"]
        render result as JSON
    }

    Long loggedUserId() {
        Long id = 0;
        if (springSecurityService.principal == null) {
            return null
        }
        if (springSecurityService.principal instanceof GrailsUser) {
            id = (springSecurityService.principal).id
        }
        return id
    }

}

class AnnounceDto {
    public Long id
    public String title
    public String subTitle
    public String description
    public Float price
    public Boolean status
    public Date dateCreated
    public Date lastUpdated
    public List illustrations
}

