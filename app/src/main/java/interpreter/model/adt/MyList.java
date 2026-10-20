package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T>{
    private List<T> list = new ArrayList<>();

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public T get(int pos) throws ADTException {
        if (pos < 0 || pos >= list.size())
            throw new ADTException("invalid list position " + pos);
        return list.get(pos);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

