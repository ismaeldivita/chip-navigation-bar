# Bubble Navigation Menu
TODO

## Usage
<img align="right" width="350" src="https://user-images.githubusercontent.com/7879502/58740933-ab013300-840b-11e9-9d03-a09f043680ce.gif">

```xml
<!-- bottom_menu.xml -->
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/home"
        android:icon="@drawable/ic_home"
        android:title="Home"
        app:bnv_iconColor="@color/home"/>
    <item
        android:id="@+id/activity"
        android:icon="@drawable/ic_activity"
        android:title="Activity"
        app:bnv_iconColor="@color/activity"/>
    <item
        android:id="@+id/favorites"
        android:icon="@drawable/ic_heart"
        android:title="Favorites"
        app:bnv_iconColor="@color/favorites" />
    <item
        android:id="@+id/settings"
        android:icon="@drawable/ic_settings"
        android:title="Settings"
        app:bnv_iconColor="@color/settings" />
</menu>

<!-- layout.xml -->
<ismaeldivita.bubblenavigation.BubbleNavigationView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    android:background="@color/white"
    app:bnv_menuResource="@menu/bottom_menu"
    app:bnv_orientationMode="horizontal" />
```

## Vertical orientation
TODO

## License
TODO
