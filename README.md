Now Then, Then Now, Now Then, Then, Then, Then Now
==================================================

Now then, what's all the fuss about?

One of the conceptual hurdles with JMock is that you have to specify the things that you expect before the actions that provoke your expected outcome.

```java
mockery.expecting(new ExpectationBuilder() {{
  oneOf(missile).launch();
}});

button.press();
```

I've been experimenting with a new formula

```java
mockery.when(new Runnable() {
  public void run() {
      button.press();
  }
}).
then(new Expectations() {{
  oneOf(missile).launch();
}});
```

I'm really liking it. I think that the runnable block will play nicely with Java 8 lambda's so reducing the boilerplate. As it is IntelliJ renders as if the lamba was there

```java
mockery.when(() -> { button.press(); }).
then(new Expectations() {{
  oneOf(missile).launch();
}});
```

The trailing dot before the then is a little odd, but seems entirely in keeping with JMock's usual punctuation abuse.

Actually, now that I think of it, the ordering explains why I kept writing helper methods on subclasses named given..(), so I'd have code  like

```java
mockery.expect(new MyExpections()
{{
  givenTurned(key1, true);
  givenTurned(key2, true);
  oneOf(missile).launch();
}});

button.press();
```

So let's add given(expectations) to mockery to allow this case -

```java
mockery.given(new MyExpectations()
{{
  isTurned(key1, true);
  isTurned(key2, true);
}}.
when(() -> { button.press(); }).
then(new Expectations()
{{
  oneOf(missile).launch();
}});

mockery.given(new MyExpectations()
{{
  isTurned(key1, true);
  isTurned(key2, false);
}}.
when(() -> { button.press(); }).
then(new Expectations()
{{
  never(missile).launch();
}});

```

Feedback? Should we add this to Mockery?
