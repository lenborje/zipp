package lb.zipp;

import java.util.ListResourceBundle;

/**
 * Default resource bundle for ZipSplit messages.
 *
 * @author lennartb
 *
 */
public class Messages_en extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        final Object[][] tmp = {
                // LOCALIZE THIS
                {"adding", "  adding "},
                {"updating", "  updating "},
                {"compressed", "compressed"},
                {"deflated", "deflated"},
                {"working", "Working on ZIP FileSystem %s, using the options '%s'%n"},
                {"output", "Zip archive: "},
                {"fszlmt", "File size limit: "},
                {"usage1", String.format("Usage: zipp %s zip_archive file [...]", Option.optionsSyntax())},
                {"zpclos", "All files/dirs entered, now closing..."},
                {"zpdone", "A zip archive of the given files/dirs has been created."},
                {"unkopt", "Error: Unknown option "},
                {"noargs", "Not enough arguments given!"},
                {"errtrav", "Error traversing directory %s"}
                // END OF MATERIAL TO LOCALIZE
        };
        return tmp;
    }

}
