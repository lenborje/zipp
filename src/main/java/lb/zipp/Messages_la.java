package lb.zipp;

import java.util.ListResourceBundle;

/**
 * Latin message resource bundle.
 * @author lennartb
 *
 */
public class Messages_la extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        final Object[][] tmp = {
                // Latin/Latina
                {"adding", " addens "},
                {"updating", "  renovans "},
                {"compressed", "comprimerus"},
                {"deflated", "deflarus"},
                {"working", "Fabricans ZIP FileSystem %s, cum parametri '%s'%n"},
                {"output", "archivum zip: "},
                {"usage1", String.format("usus: zipp %s archivum_zip documentum [...]", Option.optionsSyntax())},
                {"zpclos", "Omnia documenta lectae sint, nunc claudens..."},
                {"zpdone", "Archivum zip cum documenta aut catalogi indici creatum est."},
                {"unkopt", "Error: Optionis ignotus est!"},
                {"noargs", "Numerus parametri non satis est!"},
                {"errtrav", "Error dum legens index %s"}
        };
        return tmp;
    }

}
