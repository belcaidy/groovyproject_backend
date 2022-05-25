package project

class Illustration {

    String imageString
    static belongsTo = [annonce: Annonce]

    static constraints = {
        imageString nullable: true ,maxSize: 1073741824
        annonce nullable: false
    }
}
