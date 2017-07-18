package java_incremental_builds;

/**
 * Created by angibaudj on 13-07-17.
 */
public class ClassA {

    public void sayNull() {
        String msg = null;
        System.out.println(msg.toLowerCase());
    }

    private void unused() {
        // Do nothing
    }
}
