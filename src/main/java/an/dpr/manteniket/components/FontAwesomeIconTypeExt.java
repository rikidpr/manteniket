package an.dpr.manteniket.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.string.Strings;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import static an.dpr.manteniket.components.FontAwesomeIconTypeExtBuilder.on;


enum FontAwesomeGraphicsExt {
    bicycle,area_chart;
}

/**
 * Clase replica de de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType
 * para anyadir nuevos iconos no incluidos.
 * @author ricardo.saez-martinez@hp.com
 *
 */
public class FontAwesomeIconTypeExt  extends IconType {
    
    private static final long serialVersionUID = 1L;
    
    public static final FontAwesomeIconTypeExt bicycle = on(FontAwesomeGraphicsExt.bicycle).build();
    public static final FontAwesomeIconTypeExt area_chart= on(FontAwesomeGraphicsExt.area_chart).build();
    
    /**
     * Constructor.
     * 
     * @param cssClassName The css class name of the icon reference
     */
    FontAwesomeIconTypeExt(final String... cssClassName) {
        super(Strings.join(" ", cssClassName));
    }

    @Override
    public String cssClassName() {
        return "fa fa-" + getCssClassName();
    }

}

class FontAwesomeIconTypeExtBuilder {
    
    private FontAwesomeGraphicsExt fontAwesomeGraphic;
    
    /**
     * Rotation that can be done on an icon.
     */
    public static enum Rotation {
        flip_horizontal, flip_vertical, normal, rotate_180, rotate_270, rotate_90
    }

    /**
     * Sizes on an icon.
     */
    public static enum Size {
        two, three, four, five, large
    }

    /**
     * @param fontAwesomeGraphic icon to use in the builder
     * @return a builder for this icon
     */
    public static FontAwesomeIconTypeExtBuilder on(final FontAwesomeGraphicsExt fontAwesomeGraphic) {
        return new FontAwesomeIconTypeExtBuilder(fontAwesomeGraphic);
    }

    /**
     * Icon used in the builder.
     */
    /**
     * rotation to apply to the icon (default none).
     */
    private Rotation rotation;
    /**
     * size to apply to the icon (by default *1).
     */
    private Size size;
    /**
     * Do we have to make the icon spin?
     */
    private boolean spin;

    /**
     * @param fontAwesomeGraphic icon to use in the builder
     */
    private FontAwesomeIconTypeExtBuilder(final FontAwesomeGraphicsExt fontAwesomeGraphic) {
        this.fontAwesomeGraphic = fontAwesomeGraphic;
    }

    /**
     * @return build the icon
     */
    public FontAwesomeIconTypeExt build() {
        final List<String> styles = new ArrayList<String>();

        // replace all underscore to dashes
        styles.add(underscoresToDashes(fontAwesomeGraphic.name()));

        // add spin class?
        if (spin) {
            styles.add("fa-spin");
        }

        // add rotation class?
        if (rotation != null) {
            styles.add("fa-" + underscoresToDashes(rotation.name()));
        }

        // add size class
        if (size != null) {
            switch (size) {
                case two:
                    styles.add("fa-2x");
                    break;
                case three:
                    styles.add("fa-3x");
                    break;
                case four:
                    styles.add("fa-4x");
                    break;
                case five:
                    styles.add("fa-5x");
                    break;
                case large:
                    styles.add("fa-lg");
                    break;
            }
        }

        return new FontAwesomeIconTypeExt(styles.toArray(new String[styles.size()]));
    }

    /**
     * @param rotation rotation to apply to the icon
     * @return the builder
     */
    public FontAwesomeIconTypeExtBuilder rotate(final Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    /**
     * @param size size to apply to the icon
     * @return the builder
     */
    public FontAwesomeIconTypeExtBuilder size(final Size size) {
        this.size = size;
        return this;
    }

    /**
     * make the icon spin
     * 
     * @return the builder
     */
    public FontAwesomeIconTypeExtBuilder spin() {
        this.spin = true;
        return this;
    }

    /**
     * @param string string to work on!
     * @return the string with the underscores replace with dashes
     */
    private String underscoresToDashes(final String string) {
        return string.replace('_', '-');
    }
}