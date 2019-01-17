// Java class demonstrating the danger of mutability.

/**
 * Implementation of a mutable Integer in Java.
 */
public class StatefulInteger extends Number {
    private int state;

    public StatefulInteger (int initialState) {
	this.state = initialState;
    }

    public void setInt (int newState) {
	this.state = newState;
    }

    public int intValue () {
	return state;
    }

    public int hashCode() {
	return state * 32 + 11;
    }

    public boolean equals (Object obj) {
	return obj instanceof StatefulInteger &&
	    state == ((StatefulInteger)obj).state;
    }

    public double doubleValue () {
	return (double) state;
    }

    public float floatValue () {
	return (float) state;
    }

    public long longValue () {
	return (long) state;
    }
    
    // ...
}
