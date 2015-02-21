# Reagent-React-Router-Demo

A simple experiment on how to use [react-router](https://github.com/rackt/react-router) in clojurscript and [reagent](https://github.com/reagent-project/reagent)

## Current Status

very very alpha

## Disclamer

My understanding of clojure/clojurescript is quite limited, looking for suggestions and improvements is the main reason for this repo to exist.

# Run it

clone the [reagent-react-router](https://github.com/ghedamat/reagent-react-router) repo and run lein install

then clone this repo

```
# first run (you need npm and bower), then
bower install

# in a terminal (runs the server)
lein ring server

# in another terminal (cljs repl and continuos compilation)
lein figwheel
```

## How it works

All the code is in `src/cljs/reagent_react_router/react_router.cljs`.

React-router has a very simple api that allows you to define routes as a nested series of components [docs](https://github.com/rackt/react-router/blob/master/docs/guides/overview.md#with-react-router).

The outer component is then passed to the `run` function and by means of passing a callback we can determine where to render on the page.

This project is simply wrapping the `Link` and `RouteHandler` components provided by react-router to use them in reagent components via the amazing `adapt-react-class` api that reagent provides.

Route definition is a bit trickier because reagent's `reactify-component` wraps the component in a `<reagent-wrapper>` and that can't work (currently) with react-router.

The current implementation provides a `Route` and a `DefaultRoute` function to be used in the route definition process.

example:

```clojurescript
  (def routes
    (Route {:name "app" :path "/" :handler app-page}
      (Route {:name "about" :path "about" :handler about-page}
        (Route {:name "nested" :path ":post_id" :handler nested-page}
          (Route {:name "deeply-nested" :path ":comment_id" :handler deeply-nested-page})))
      (DefaultRoute {:handler default-page-meta})))
```

Finally a `run-router` function is provided to boot up the app

```clojurscript
(defn init! []
  (run-router document.body routes))
```

### A note on dynamic segments

React-router has excellent support for [dynamic segments](https://github.com/rackt/react-router/blob/master/docs/guides/overview.md#with-react-router), normally they're used by including a mixing in the react-component that needs them BUT there's also an option that allows passing them down from the topmost component.

That's what is begin done for now.
any reagent component nested under the route will receive a `params` jsObject, every time a `RouteHandler` is defined, the map should be passed as its argument so that is available to the nested components.

The params in the jsObject are accessed as any other jsObject in clojurescript.

Note that if params are used in a component and the params object changes the component tree will be rerendered from there downwards. This is probably something that could be made more efficent...

## Whishlist/TODO

Ideally the routes definition api would be more close to normal reagent templates

```clojurscript
  (defn routes[]
    [Route {:name "app" :path "/" :handler app-page}
     [Route {:name "about" :path "about" :handler about-page}]])
```

but I'm still not clear on how to get there

I haven't used this in any real apps yet so it's quite likely that some of the use cases covered by the router are missing/won't work properly.

* Query params support hasn't been tested at all
* Implement `NotFoundRoute` and `Redirect`


# Feedback

Please let me know what you think, if this makes sense at all and how it could be improved!

