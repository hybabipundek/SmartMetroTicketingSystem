package payment;

/**
 * Interface that defines the common payment operation used by different payment methods.
 */
public interface Payment {
    boolean pay(double amount);
}
