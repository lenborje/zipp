package lb.zipp;

import java.util.ListResourceBundle;

/**
 * German message resource bundle.
 * @author lennartb
 *
 */
public class Messages_de extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        final Object[][] tmp = {
                // German/Deutsch
                {"adding", " zuf\u00fcgen "},
                {"updating", "  auktualisieren "},
                {"compressed", "komprimiert"},
                {"deflated", "entleert"},
                {"working", "Arbeiten mit ZIP FileSystem %s, verwenden die Optionen '%s'%n"},
                {"output", "Zip-Archiv: "},
                {"usage1", String.format("Gebrauch: zipp %s Zip-Archiv Datei [...]", Option.optionsSyntax())},
                {"zpclos", "Alle Dateien registeiert; jetzt schliessen..."},
                {"zpdone", "Ein Zip-Archiv mit den angegebenen Dateien / Verzeichnisse ist gemacht."},
                {"unkopt", "Fehler: Unbekannte lb.zipp.Option"},
                {"noargs", "Nicht genug parametern angegeben!"},
                {"errtrav", "Fehler während lesen des Kataloges %s"}
        };
        return tmp;
    }

}
