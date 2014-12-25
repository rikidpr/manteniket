package an.dpr.manteniket;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import an.dpr.manteniket.HomePage;
import an.dpr.manteniket.WicketApplication;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage {
    private WicketTester tester;

    @Before
    public void setUp() {
	tester = new WicketTester(new WicketApplication());
	ApplicationContext appcxt = new ClassPathXmlApplicationContext("applicationContext.xml");
        //registrar los daos (bikedao...) generalDao = (GeneralDao) appcxt.getBean("GeneralDao");
        // 2. setup mock injection environment
//        AnnotApplicationContextMock appctx = new AnnotApplicationContextMock();
//        appctx.putBean("GeneralDao", generalDao);
//
//        WicketApplication wicketPersistanceApplication = new WicketApplication();
//        wicketPersistanceApplication
//                .setSpringComponentInjector(new SpringComponentInjector(
//                        wicketPersistanceApplication, appctx));
//        wicketTester = new WicketTester(wicketPersistanceApplication);
    }

//    @Test
    public void homepageRendersSuccessfully() {
	// start and render the test page
	tester.startPage(HomePage.class);

	// assert rendered page class
	tester.assertRenderedPage(HomePage.class);
    }
}
