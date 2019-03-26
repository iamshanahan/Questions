package questions;

import org.junit.Test;
import static org.junit.Assert.*;

public class AvmExceptionTest {
    @Test public void testExceptionWorks() {
        try {
            throw new AmvException();
        } catch( AmvException e ) {
            //success
            // TODO what's that "empty body intentional" annotation?
        }
    }
}
