# Bubble Navigation Menu
TODO

## Usage
<img align="right" width="350" src="https://user-images.githubusercontent.com/7879502/58746357-4f5b9780-8455-11e9-8efd-62f064ce0a34.gif">

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
    app:bnv_menuResource="@menu/bottom_menu" />
```
<br clear="right"/>

## Public API and custom attributes
TODO

## Vertical orientation

`BubbleNavigationView` supports a vertical orientation mode. This is very useful for 
tablets or devices with large screens.

<img width="350" align="right" src="https://user-images.githubusercontent.com/7879502/58746359-52568800-8455-11e9-9fa9-4cf49abeb4ee.gif"/>

Just add the attribute `bnv_orientationMode` to your xml:
```xml
<ismaeldivita.bubblenavigation.BubbleNavigationView
    android:id="@+id/bottom_menu"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    app:bnv_menuResource="@menu/test"
    app:bnv_orientationMode="vertical" />
```
... or programmatically call the method `setMenuOrientation` before inflate the menu:
```kotlin
menu.setMenuOrientation(MenuOrientation.VERTICAL)
menu.setMenuResource(R.menu.my_menu)
```

 >**Note:** The view exposes methods to expand and collapse the menu but we don't provide the implementation for the toggle button. Check the sample for a basic implementation.

 <br clear="right"/>

## License
TODO
