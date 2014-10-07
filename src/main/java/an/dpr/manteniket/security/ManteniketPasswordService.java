package an.dpr.manteniket.security;

import java.security.MessageDigest;

import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase para encryptado del password
 * 
 * @author rsaez
 * 
 */
public class ManteniketPasswordService implements PasswordService {
    
    private static final Logger log = LoggerFactory.getLogger(ManteniketPasswordService.class);
    private MessageDigest msgDigest;

    @Override
    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
	boolean ret = false;
	if (submittedPlaintext != null && encrypted != null) {
	    String submittedEncrypted = encryptPassword(submittedPlaintext);
	    ret = submittedEncrypted.equals(encrypted);
	}
	return ret;
    }

    @Override
    public String encryptPassword(Object plaintextPassword)
	    throws IllegalArgumentException {
	log.debug("encriptando pass");
	String ret = null;
	Sha1Hash hash = new Sha1Hash(plaintextPassword);
	ret = refuerzoSeguridadExtra(hash.toString());
	return ret;
    }
    
    private static String refuerzoSeguridadExtra(String hash) {
	StringBuilder pass = new StringBuilder();
	pass.append(hash.substring(0,10));
	pass.append("MAN");
	pass.append(hash.substring(10,20));
	pass.append("TENI");
	pass.append(hash.substring(20,30));
	pass.append("KET");
	pass.append(hash.substring(30,40));
	return pass.toString();
    }

    public static void main(String[] args){
	String texto = "riki";
	Md5Hash pass = new Md5Hash(texto);
	System.out.println(pass.toString());
	System.out.println("62dd7e80fdfcb966cac9c849268c61d9");
	
	Sha1Hash hash = new Sha1Hash(texto);
	System.out.println(hash);
	System.out.println(refuerzoSeguridadExtra(hash.toString()));
	System.out.println("2bdde01c07MANf1264c02c6TENIb31eb67d79KET18c2defe8b");
    }
    

    public MessageDigest getMsgDigest() {
        return msgDigest;
    }

    public void setMsgDigest(MessageDigest msgDigest) {
        this.msgDigest = msgDigest;
    }
};