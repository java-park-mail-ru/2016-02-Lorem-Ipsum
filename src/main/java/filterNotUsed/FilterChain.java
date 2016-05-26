package filterNotUsed;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Installed on 20.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class FilterChain implements IFilterChain {

    private List<IFilter> filters = new LinkedList<>();

    @Override
    public void addFilter(IFilter filter) {
        filters.add(filter);
    }

    @Override
    public boolean check() {
        boolean passed = true;
        for (IFilter filter : filters) {
            if(!filter.check()) {
                passed = false;
                break;
            }
        }
        return passed;
    }
}
