package questions;

import org.junit.Test;
import static org.junit.Assert.*;

public class AvmExceptionTest {
    @Test public void testExceptionWorks() {
        try {
            throw new AvmException("Test Message");
        } catch( AvmException e ) {
            //success
            // TODO what's that "empty body intentional" annotation?
        }
    }
}
