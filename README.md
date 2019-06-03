# Chip Navigation Bar
A navigation bar widget inspired on Google [Bottom Navigation](https://material.io/design/components/bottom-navigation.html) mixed with [Chips](https://material.io/design/components/chips.html) component.

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
        app:cnb_iconColor="@color/home"/>
    <item
        android:id="@+id/activity"
        android:icon="@drawable/ic_activity"
        android:title="Activity"
        app:cnb_iconColor="@color/activity"/>
    <item
        android:id="@+id/favorites"
        android:icon="@drawable/ic_heart"
        android:title="Favorites"
        app:cnb_iconColor="@color/favorites" />
    <item
        android:id="@+id/settings"
        android:icon="@drawable/ic_settings"
        android:title="Settings"
        app:cnb_iconColor="@color/settings" />
</menu>

<!-- layout.xml -->
<ismaeldivita.chipnavigation.ChipNavigationBar
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    android:background="@color/white"
    app:cnb_menuResource="@menu/bottom_menu" />
```
<br clear="right"/>

## XML custom attributes
### Menu xml custom attributes

| attribute|description|default|
|----------|-------------|------|
| `cnb_disabledColor` |color used for the disable state|`R.attr.colorButtonNormal`|
| `cnb_unselectedColor` |color used for unselected state|`#696969`|

```xml
<menu
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:cnb_disabledColor="#606060"
    app:cnb_unselectedColor="@color/my_unselected_color">

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
| `cnb_iconColor`|color used to tint the icon on selected state|`R.attr.colorAccent`|
| `cnb_icontTintMode`|`PorterDuff.Mode` to apply the color. Possible values: [src_over, src_in, src_atop, multiply, screen]|`null`|
| `cnb_textColor`|color used for the label on selected state|same color used for `cnb_iconColor`|
| `cnb_backgroundColor`|color used for the chip background|same color used for `cnb_iconColor` with 15% alpha

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/home"
        android:enabled="true"
        android:icon="@drawable/ic_home"
        android:title="@string/home"
        app:cnb_backgroundColor="@color/home_chip"
        app:cnb_iconColor="@color/home_icon"
        app:cnb_iconTintMode="src_in"
        app:cnb_textColor="@color/home_label" />

        ...

</menu>
```

### ChipNavigationBar xml custom attributes
| attribute|description|default|
|----------|-------------|------ |
| `cnb_menuResource`|menu resource file|optional since you can set this programmatically|
| `cnb_hideOnScroll`|flag to enable the reveal and dismiss behavior on user scrolls. Only effective if the view is inside a `CoordinatorLayout`|false|
| `cnb_orientationMode`|menu orientation. Posisble values: [horizontal, vertical]|horizontal|
| `cnb_addBottomInset`|property to enable the sum of the window insets on the current bottom padding, useful when you're using the translucent navigation bar|false|
| `cnb_addTopInset`|property to enable the sum of the window insets on the current bottom padding, useful when you're using the translucent status bar with the vertical mode|false|
| `cnb_addLeftInset`|property to enable the sum of the window insets on the current start padding, useful when you're using the translucent navigation bar with landscape|false|
| `cnb_addRightInset`|property to enable the sum of the window insets on the current end padding, useful when you're using the translucent navigation bar with landscape|false|

```xml
<ismaeldivita.chipnavigation.ChipNavigationBar
    android:id="@+id/menu"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cnb_menuResource="@menu/bottom_menu"
    app:cnb_hideOnScroll="true"
    app:cnb_orientationMode="horizontal"
    app:cnb_addBottomInset="false"
    app:cnb_addLeftInset="false"
    app:cnb_addRightInset="false"
    app:cnb_addTopInset="false" />
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

`ChipNavigationBar` supports a vertical orientation mode. This is very useful for
tablets or devices with large screens.

<img width="350" align="right" src="https://user-images.githubusercontent.com/7879502/58746359-52568800-8455-11e9-9fa9-4cf49abeb4ee.gif"/>

Just add the attribute `cnb_orientationMode` to your xml:
```xml
<ismaeldivita.chipnavigation.ChipNavigationBar
    android:id="@+id/bottom_menu"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    app:cnb_menuResource="@menu/test"
    app:cnb_orientationMode="vertical" />
```
... or programmatically call the method `setMenuOrientation` before inflate the menu:
```kotlin
menu.setMenuOrientation(MenuOrientation.VERTICAL)
menu.setMenuResource(R.menu.my_menu)
```

 >**Note:** The view exposes methods to expand and collapse the menu but we don't provide the implementation for the toggle button. Check the sample for a basic implementation.

 <br clear="right"/>

## License
MIT License

Copyright (c) 2019 Ismael Di Vita

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
