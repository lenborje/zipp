package lb.zipp;

import java.util.ListResourceBundle;

/**
 * Swedish message resource bundle.
 * @author lennartb
 *
 */
public class Messages_sv extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        final Object[][] tmp = {
                // Swedish/Svenska
                {"adding", "  lägger till "},
                {"updating", "  updaterar "},
                {"compressed", "komprimerat"},
                {"deflated", "hopslaget"},
                {"working", "Arbetar på ZIP FileSystem %s, med väljarna '%s'%n"},
                {"output", "Zip-arkiv: "},
                {"usage1", String.format("Användning: zipp %s zip-arkiv fil [...]", Option.optionsSyntax())},
                {"zpclos", "Alla filer/kataloger processade, stänger arkivet..."},
                {"zpdone", "Ett zip-arkiv av angivna filer/kataloger har skapats."},
                {"unkopt", "Fel: okänd väljare "},
                {"noargs", "Inte tillräckligt antal parametrar!"},
                {"errtrav", "Fel under katalogläsning av %s"}
        };
        return tmp;
    }

}

