package q.rorbin.verticaltablayout.adapter;

import q.rorbin.verticaltablayout.widget.TabView;

public interface TabAdapter {
    int getCount();

    TabView.TabBadge getBadge(int position);

    TabView.TabIcon getIcon(int position);

    TabView.TabTitle getTitle(int position);

    int getBackground(int position);
}
