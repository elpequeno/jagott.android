package de.huerse.jagott;

/**
 * Kontaktdaten mit Englisch-Deutsch-Mix
 */
public class KontaktData {
    public String name;
    public String mail;
    public int photoId;
    public String job;
    public String about;

    KontaktData(String name, String mail, int photoId, String job, String about) {
        this.name = name;
        this.mail = mail;
        this.photoId = photoId;
        this.job = job;
        this.about = about;
    }

    static final String aboutMichael = "Ich bin Michael Bayer und komme aus dem schönen Schwoabeländle und bin mit meiner wundervollen Frau Manuela verheiratet" +
            "\n\nWir wohnen in der Nähe von Ludwigsburg und ich bin als Bezirksjugendreferent im Kirchenbezirk Ditzingen tätig. Die Kinder- und Jugendarbeit ist " +
            "für mich nicht nur ein Beruf, sondern eine Aufgabe in die ich mich gerne mit Leidenschaft und Saubock reinhänge! \n\n" +
            "Bei Ja-Gott bin ich von Anfang an dabei und freue mich wie ein Schneekönig, dass wir immer noch aktiv sind.\n";

    static final String aboutLena ="Ich heiße Lena Vach und wohne zusammen mit meiner kleinen Familie in Speyer. \n\n" +
            "Raus in die Natur, in unseren Garten, Ski fahren oder Kanu fahren, mit viel Ruhe ein gutes Buch lesen, Kuchen backen und volle Gutzldosen " +
            "in der Adventszeit lösen bei mir Begeisterung aus!\n" +
            "Von Beruf bin ich Pfarrerin. Meine Gemeinde ist die Gedächtniskirchengemeinde in Speyer - Wemn ihr in der Nähe seid: herzliche Einladung in " +
            "unsere Gottesdienste! :-)\n" + "Für ja-gott schreibe ich schon seit dem Studium Andachten. Und dabei habe ich selbst viel gelernt!\n";

    static final String aboutCarina = "Ich heiße Carina Pfeiffer und wohne mit meinem Mann in Tuttlingen. Aber eigentlich komme ich aus der wunderschönen Pfalz. \uD83D\uDE00\n" +
            "Ich bin Sozialpädagogin und habe drei Jahre an der Bibelschule Brake studiert. \n\n" +
            "Ich liebe es mit einem guten Buch in der Hängematte zu liegen und völlig abzutauchen. Außerdem kreiere ich gerne coole und leckere Torten und an meinem Klavier kann ich die Welt um mich herum vergessen. \uD83D\uDE0A\n" +
            "In der Gemeinde ärgere ich jeden Freitag unsere Teenies im Teenkreis und spiele im Lobpreisteam. \n\n" +
            "Bei Ja- Gott bin ich nun auch schon einige Jahre dabei und schreibe dort Andachten.";

    static final String aboutKerstin = "Hey Leute, ich bin Kerstin. Momentan genieße ich ein buntes Studentenleben an der CVJM-Hochschule in Kassel und lerne was über Gemeinde –, Religionspädagogik "+
            "und Soziale Arbeit. \n\n Nebenbei gehe ich gerne joggen oder tanzen, zocke bei LAN-Partys „Age of Empires II“ oder quatsche mit Freunden bis tieeef in die " +
            "Nacht. Meine Heimatgemeinde im Münsterland, Familie und Freunde haben viel dazu beigetragen, dass ich einen persönlichen Glauben kennengelernen konnte. " +
            "Gedanken und Erlebnisse daraus möchte ich gerne durch JA-GOTT weitergeben.";

    static final String aboutEva = "Ich heiße Eva Dorothée. Natur, schöne Dinge, gute Bücher, Klettern, Flohmärkte, Erdbeer- Sojamilchshakes und kreativ kombinierte Worte machen mich glücklich. " +
            "\n\nWenn ich nicht gerade eines der genannten Dinge genieße, " +
            "studiere ich Soziale Arbeit und Religions- und Gemeindepädagogik in Kassel. Bei Ja Gott bringe ich mich als Autorin ein. ";

    static final String aboutMarcel = "Hi, ich heiße Marcel, bin 22 Jahre alt und komme ursprünglich aus der Kleinstadt Zittau in Sachsen. Momentan studiere ich Soziale Arbeit und Gemeindepädagogik und wohne in einer WG in Kassel. Ich liebe es Gitarre zu spielen und zu singen, besonders wenn es mit meiner Band ist. An sonnigen Tagen cruise ich oft mit meinen Inlineskates durch die Stadt und power mich so richtig aus. \n" +
            "\n\nJesus begeistert mich und darum freue ich mich total darauf für Ja-Gott Andachten schreiben zu dürfen und euch teilhaben zu lassen an seinem Wort.\n";

    static final String aboutAlexander =  "Ich bin Alexander Blümel, gebürtiger Sauerländer und zurzeit Student der Sozialen Arbeit und der Religions- und Gemeindepädagogik" +
            "an der CVJM Hochschule in Kassel.\n\nIch bin eigentlich immer fröhlich, sehr anfällig für Ohrwürmer und für jeden Spaß zu haben. " +
            "Begeistern lasse ich mich für fast alle Sportarten – sowohl aktiv, als auch passiv vor dem Fernseher. Natur, Reisen und Abenteuer " +
            "sind meine zweite Leidenschaft, was gut damit zusammen passt, dass ich nebenbei als Wildnis- und Erlebnispädagoge arbeite. " +
            "\n\nDoch mein Herz schlägt dafür, Menschen von Jesus zu erzählen, was mich letztlich als Autor ins Ja-Gott-Team führte.";

    static final String aboutAndre =  "Hola, mein Name ist André Klein und ich wohne im schönen Heidelberg. " +
            "Seit Juni 2016 promoviere ich als Medizininformatiker am Deutschen Krebsforschungszentrum in der Abteilung für medizinische Bildverarbeitung. " +
            "Meine geistliche Heimat habe ich im CVJM Katzweiler." +
            "\n\nAuch wenn ich Informatiker bin, bin ich am liebsten draußen. Ich liebe Jungschararbeit, spiele Fanfare, Gitarre und seit Kurzem American Football." +
            "\n\nBei JA-GOTT bin ich für die Homepage und die Android-App verantwortlich. Ich freue mich immer über Feedback und Anregungen.\n";

    static final String aboutMartin = "Mein Name ist Martin Forell und ich wohne zur Zeit im schönen Karlsruhe :) Hier studiere ich einerseits Informationswirtschaft am KIT " +
            "(ähnlich wie Wirtschaftsinformatik :) ) und arbeite währenddessen als Gitarrenlehrer an einer Musikschule und als Medieninformatiker am Zentrum " +
            "für mediales Lernen.\n\nIn meiner Freizeit liebe ich es Gitarre zu spielen und Sport zu machen, außerdem fahre ich gerne zu abgelegenen Orten, " +
            "um diese früh morgens zum Sonnenaufgang mit meiner Kamera zu fotografieren.\n\nBei JaGott bin ich zur Zeit für die iOS App und die Homepage zuständig. ";


}
