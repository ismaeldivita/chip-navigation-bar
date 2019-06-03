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

## XML custom attributes
### Menu xml custom attributes

| attribute|description|default|
|----------|-------------|------|
| `bnv_disabledColor` |color used for the disable state|`R.attr.colorButtonNormal`|
| `bnv_unselectedColor` |color used for unselected state|`#696969`|

```xml
<menu
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:bnv_disabledColor="#606060"
    app:bnv_unselectedColor="@color/my_unselected_color">

    ...

</menu>
```
### MenuItem xml custom attributes
| attribute|description|default|
|----------|-------------|------ |
| `android:id`|id|**required**|
| `android:enabled`|enabled state|true|
| `android:icon`|icon drawable|**required**|
| `android:title`|label string|**required**|
| `bnv_iconColor`|color used to tint the icon on selected state|`R.attr.colorAccent`|
| `bnv_icontTintMode`|`PorterDuff.Mode` to apply the color. Possible values: [src_over, src_in, src_atop, multiply, screen]|`null`|
| `bnv_textColor`|color used for the label on selected state|same color used for `bnv_iconColor`|
| `bnv_backgroundColor`|color used for the bubble background|same color used for `bnv_iconColor` with 15% of alpha

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/home"
        android:enabled="true"
        android:icon="@drawable/ic_home"
        android:title="@string/home"
        app:bnv_backgroundColor="@color/home_bubble"
        app:bnv_iconColor="@color/home_icon"
        app:bnv_iconTintMode="src_in"
        app:bnv_textColor="@color/home_label" />

        ...

</menu>
```

### BubbleNavigationView xml custom attributes
| attribute|description|default|
|----------|-------------|------ |
| `bnv_menuResource`|menu resource file|optional since you can set this programmatically|
| `bnv_hideOnScroll`|flag to enable the reveal and dismiss behavior on user scrolls. Only effective if the view is inside a `CoordinatorLayout`|false|
| `bnv_orientationMode`|menu orientation. Posisble values: [horizontal, vertical]|horizontal|
| `bnv_addBottomInset`|property to enable the sum of the window insets on the current bottom padding, useful when you're using the translucent navigation bar|false|
| `bnv_addTopInset`|property to enable the sum of the window insets on the current bottom padding, useful when you're using the translucent status bar with the vertical mode|false|
| `bnv_addLeftInset`|property to enable the sum of the window insets on the current start padding, useful when you're using the translucent navigation bar with landscape|false|
| `bnv_addRightInset`|property to enable the sum of the window insets on the current end padding, useful when you're using the translucent navigation bar with landscape|false|

```xml
<ismaeldivita.bubblenavigation.BubbleNavigationView
    android:id="@+id/menu"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:bnv_menuResource="@menu/bottom_menu"
    app:bnv_hideOnScroll="true"
    app:bnv_orientationMode="horizontal"
    app:bnv_addBottomInset="false"
    app:bnv_addLeftInset="false"
    app:bnv_addRightInset="false"
    app:bnv_addTopInset="false" />
```
## Public API

| method|description|
|---------------|----|
|`setMenuResource(@MenuRes menuRes: Int)` | Inflate a menu from the specified XML resource|
|`setMenuOrientation(menuOrientation: MenuOrientation)` | Set the menu orientation|
|`setItemEnabled(id: Int, isEnabled: Boolean)` | Set the enabled state for the menu item with the provided [id]|
|`setItemSelected(id: Int)` | Remove the selected state from the current item and set the selected state to true for the menu item with the [id]|
|`setHideOnScroll(isEnabled: Boolean)`|Set the enabled state for the hide on scroll [CoordinatorLayout.Behavior]. The behavior is only active when orientation mode is HORIZONTAL|
|`setOnItemSelectedListener(listener: OnItemSelectedListener)`|Register a callback to be invoked when a menu item is selected|
|`show()`|Show menu if the orientationMode is HORIZONTAL otherwise, do nothing|
|`hide()`|Hide menu if the orientationMode is HORIZONTAL otherwise, do nothing|
|`collapse()`|Collapse the menu items if orientationMode is VERTICAL otherwise, do nothing|
|`expand()`|Expand the menu items if orientationMode is VERTICAL otherwise, do nothing|

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
