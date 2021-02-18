package streaming.core.util;

public class Pair<U, V>
{
    public final U left;
    public final V right;

    private Pair(U left, V right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!left.equals(pair.left))
            return false;
        return right.equals(pair.right);
    }

    @Override
    public int hashCode()
    {
        return 31 * left.hashCode() + right.hashCode();
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + left +
                ", second=" + right +
                '}';
    }

    public U getLeft() {
        return left;
    }

    public V getRight() {
        return right;
    }

    public static <U, V> Pair <U, V> of(U a, V b)
    {
        return new Pair<>(a, b);
    }
}

