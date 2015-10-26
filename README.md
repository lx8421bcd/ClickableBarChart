ClickableHistogram
========
An android custom histogram view with animation and clickable columns.

##Features
* initializating animation    
 ![image](https://github.com/ShonLin/ClickableHistogram/blob/master/screenshots/ViewAnimation.gif)    
* handling the click event    
 ![image](https://github.com/ShonLin/ClickableHistogram/blob/master/screenshots/click.gif)
* switching data source   
 ![image](https://github.com/ShonLin/ClickableHistogram/blob/master/screenshots/reload.gif)

##Usage
* The Project are stil being test, please use the SNAPSHOT version. just add the dependency and maven url to your build.gradle :
```
dependencies {
  repositories {
        maven {url "https://oss.sonatype.org/content/repositories/snapshots/"}
    }
    compile 'com.github.shonlin:clickablehistogram:1.0.2-SNAPSHOT'
}
```
* The view has following special attribution  
columnWidth : width of each column    
columnMargin : left and right margin of each column   
axisWidth : width of x-axis   
textSize : text size of column name and value   
columnColor : color of each column    
axisColor : color of x-axis   
textColor : text color of name and value    

* Add data to view
```java
ArrayList<ClickableHistogram.ColumnData> dataSource = new ArrayList<>();
//init data......
clickableHistogram.setDataSource(dataSource);
```

* Set column's click listener
```java
clickableHistogram.setOnColumnClicklistener(new ClickHistogram.OnColumnClickListener() {
  @Override
  public void onColumnClick(View v, int position, ClickableHistogram.ColumnData data) {
    //do something......
  }
});
```
##Caveates
this project supported for `minSDKVersion >＝ 8`,but it not supported column self-increase animation which is used in minSDKVersion older than API 11.
