# funky-apt
[tiny demo thing] A super-duper-simple annotation processor which is executed to be debugged. And it generates a Builder thing for non-private instance variables.

```
c:\Development\HomeProjects\funky-apt\src\main\java\com\zhuinden\App.java:10: Note: Element traversed: com.zhuinden.App
public class App {
       ^
Note: Package Name [com.zhuinden], Class Name [com.zhuinden.App], Simple Name [App]
```

So I have this:

``` java
@Funk
public class App {
    String hello;
    String world;

    ...
```

And generates this:

``` java
public final class AppBuilder {
  private String hello;

  private String world;

  public AppBuilder setHello(String hello) {
    this.hello = hello;
    return this;
  }

  public AppBuilder setWorld(String world) {
    this.world = world;
    return this;
  }

  public App build() {
    App app = new App();
    app.hello = this.hello;
    app.world = this.world;
    return app;
  }
}
```
