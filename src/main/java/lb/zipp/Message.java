package lb.zipp;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Defines zipp messages.
 *
 * @author Lennart Börjeson
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public enum Message {
    adding, updating, compressed, deflated, working,
    output, fszlmt, usage1, zpclos, zpdone, unkopt,
    noargs, errtrav, cretemp, tstadd, tstclose, tsttotal, tstproc, zipdone0;

    private static final Map<Message, String> DEFAULT = new HashMap<>();
    private static final Map<Message, String> DEUTSCH = new HashMap<>();
    private static final Map<Message, String> SVENSKA = new HashMap<>();
    private static final Map<Message, String> LATINA = new HashMap<>();

    private static final ConcurrentMap<String, Map<Message, String>> BUNDLES = new ConcurrentHashMap<>();

    static {
        DEFAULT.put(adding, "  adding ");
        DEFAULT.put(updating, "  updating ");
        DEFAULT.put(compressed, "compressed");
        DEFAULT.put(deflated, "deflated");
        DEFAULT.put(working, "Working on ZIP FileSystem %s, using the options '%s'%n");
        DEFAULT.put(output, "Zip archive: ");
        DEFAULT.put(fszlmt, "File size limit: ");
        DEFAULT.put(usage1, String.format("Usage: zipp %s zip_archive file [...]", Option.optionsSyntax()));
        DEFAULT.put(zpclos, "All files/dirs entered, now closing...");
        DEFAULT.put(zipdone0, "Done!");
        DEFAULT.put(zpdone, "A zip archive of the given files/dirs has been created.");
        DEFAULT.put(unkopt, "Error: Unknown option ");
        DEFAULT.put(noargs, "Not enough arguments given!");
        DEFAULT.put(errtrav, "Error traversing directory %s");
        DEFAULT.put(cretemp, "Creating %d temporary files, using %d processors (ignoring file args)%n");
        DEFAULT.put(tstadd, "Add: %d ms CPU in %d ms, ratio %f%n");
        DEFAULT.put(tstclose, "Close: %d ms CPU in %d ms, ratio %f%n");
        DEFAULT.put(tsttotal, "Total: %d ms CPU in %d ms, ratio %f%n");
        DEFAULT.put(tstproc, "Number of available processors is %d%n");

        BUNDLES.put("", DEFAULT);

        DEUTSCH.put(adding, " zuf\u00fcgen ");
        DEUTSCH.put(updating, "  auktualisieren ");
        DEUTSCH.put(compressed, "komprimiert");
        DEUTSCH.put(deflated, "entleert");
        DEUTSCH.put(working, "Arbeiten mit ZIP FileSystem %s, verwenden die Optionen '%s'%n");
        DEUTSCH.put(output, "Zip-Archiv: ");
        DEUTSCH.put(usage1, String.format("Gebrauch: zipp %s Zip-Archiv Datei [...]", Option.optionsSyntax()));
        DEUTSCH.put(zpclos, "Alle Dateien registeiert; jetzt schliessen...");
        DEUTSCH.put(zipdone0, "Fertig!");
        DEUTSCH.put(zpdone, "Ein Zip-Archiv mit den angegebenen Dateien / Verzeichnisse ist gemacht.");
        DEUTSCH.put(unkopt, "Fehler: Unbekannte lb.zipp.Option");
        DEUTSCH.put(noargs, "Nicht genug parametern angegeben!");
        DEUTSCH.put(errtrav, "Fehler während lesen des Kataloges %s");
        DEUTSCH.put(cretemp, "Schafft %d temporäre Dateien, durch %d procezzesoren verwenden (Datei-argumenten sind ignoriert geworden)%n");
        DEUTSCH.put(tstadd, "Addieren: %d ms CPU in %d ms, ratio %f%n");
        DEUTSCH.put(tstclose, "Schließen: %d ms CPU in %d ms, ratio %f%n");
        DEUTSCH.put(tsttotal, "Im Gesamt: %d ms CPU in %d ms, ratio %f%n");
        DEUTSCH.put(tstproc, "Anzahl verfügbare procezzoren ist %d%n");

        BUNDLES.put(Locale.GERMAN.getLanguage(), DEUTSCH);

        SVENSKA.put(adding, "  lägger till ");
        SVENSKA.put(updating, "  updaterar ");
        SVENSKA.put(compressed, "komprimerat");
        SVENSKA.put(deflated, "hopslaget");
        SVENSKA.put(working, "Arbetar på ZIP FileSystem %s, med väljarna '%s'%n");
        SVENSKA.put(output, "Zip-arkiv: ");
        SVENSKA.put(usage1, String.format("Användning: zipp %s zip-arkiv fil [...]", Option.optionsSyntax()));
        SVENSKA.put(zpclos, "Alla filer/kataloger processade, stänger arkivet...");
        SVENSKA.put(zipdone0, "Klart!");
        SVENSKA.put(zpdone, "Ett zip-arkiv av angivna filer/kataloger har skapats.");
        SVENSKA.put(unkopt, "Fel: okänd väljare ");
        SVENSKA.put(noargs, "Inte tillräckligt antal parametrar!");
        SVENSKA.put(errtrav, "Fel under katalogläsning av %s");
        SVENSKA.put(cretemp, "Skapar %d temporära filer, m.h.a. %d processorer (ignorerar fil-argument) %n");
        SVENSKA.put(tstadd, "Addera: %d ms CPU på %d ms, ratio %f%n");
        SVENSKA.put(tstclose, "Stänga: %d ms CPU på %d ms, ratio %f%n");
        SVENSKA.put(tsttotal, "Total: %d ms CPU på %d ms, ratio %f%n");
        SVENSKA.put(tstproc, "Antal tillgängliga processorer är %d%n");

        BUNDLES.put(new Locale("sv").getLanguage(), SVENSKA);

        LATINA.put(adding, " addens ");
        LATINA.put(updating, "  renovans ");
        LATINA.put(compressed, "comprimerus");
        LATINA.put(deflated, "deflarus");
        LATINA.put(working, "Fabricans ZIP FileSystem %s, cum parametri '%s'%n");
        LATINA.put(output, "archivum zip: ");
        LATINA.put(usage1, String.format("usus: zipp %s archivum_zip documentum [...]", Option.optionsSyntax()));
        LATINA.put(zpclos, "Omnia documenta lectae sunt, nunc claudeo...");
        LATINA.put(zipdone0, "Egi!");
        LATINA.put(zpdone, "Archivum zip cum documenta aut catalogi indici creatum est.");
        LATINA.put(unkopt, "Error: Optionis ignotus est!");
        LATINA.put(noargs, "Numerus parametri non satis est!");
        LATINA.put(errtrav, "Error dum legens index %s");
        LATINA.put(cretemp, "Facio %d documentum temporarium, cum %d processore auxiliariis (parametri documentae praetermissi sunt)%n");
        LATINA.put(tstadd, "Addere: %d ms CPU in %d ms, ratio %f%n");
        LATINA.put(tstclose, "Claudere: %d ms CPU in %d ms, ratio %f%n");
        LATINA.put(tsttotal, "Summa: %d ms CPU in %d ms, ratio %f%n");
        LATINA.put(tstproc, "Numerus processore est %d%n");

        BUNDLES.put(new Locale("la").getLanguage(), LATINA);
    }

    public static String getMessage(Message m) {
        Locale currentLocale = Locale.getDefault();
        Map<Message, String> bundle = BUNDLES.getOrDefault(currentLocale.getLanguage(), DEFAULT);
        return bundle.getOrDefault(m, m.toString());
    }
}