package filterNotUsed;

/**
 * Created by Installed on 20.05.2016.
 */
public interface IFilterChain {
    void addFilter(IFilter filter);
    boolean check();
}
