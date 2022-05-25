package project

class Annonce {

    String title
    String subTitle
    String description
    Float price
    Boolean status = Boolean.FALSE
    Date dateCreated
    Date lastUpdated

    static belongsTo = [author: User]

    static hasMany = [illustrations: Illustration]

    static constraints = {
        title nullable: false, blank: false, size: 2..255
        description nullable: true, blank: true, minSize: 8
        price nullable: false, min: 0F
        status nullable: false
        subTitle nullable: true
    }

    static mapping = {
        description type: 'text'
    }

    Annonce addIllustrations(Illustration illustration) {
        if (illustrations == null) {
            illustrations = new LinkedList<>() as Set<Illustration>
        }
        illustrations.add(illustration)
        return this
    }
}
