package an.dpr.manteniket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.LabelBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.LabelType;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.Carousel;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.CarouselImage;

public class MainPage extends ManteniketPage {

    private static final Logger log = LoggerFactory.getLogger(MainPage.class);
    private Label lblBienvenida;
    
    public MainPage(){
	super();		
//	getMenuPanel().setVisible(false);
	lblBienvenida = new Label("lblBienvenida", "epei a manteniket");
	
	
	log.debug("add label behaviour");
	Component comp = lblBienvenida;
	comp.add(new LabelBehavior(LabelType.Success));
	add(lblBienvenida);
	
	List<CarouselImage> images = new ArrayList<CarouselImage>();
	//ad images
	images.add(new CarouselImage("/img/img1.JPG"));
	images.add(new CarouselImage("/img/img2.JPG"));
	Carousel carousel = new Carousel("carousel", images);
	add(carousel);
	
    }
}
