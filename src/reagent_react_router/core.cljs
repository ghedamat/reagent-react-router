(ns reagent-react-router.core
   (:require [reagent.core :as reagent :refer [atom]]
             [react-router :as router])) ;// this should become [cljsjs.react-router]

(def Link (reagent/adapt-react-class js/ReactRouter.Link))
(def RouteHandler (reagent/adapt-react-class js/ReactRouter.RouteHandler))

(defn Route
  [params & args]
  (let [route (.createFactory js/React js/ReactRouter.Route)]
    (apply route
           (js-obj
             "name" (:name params)
             "path" (:path params)
             "handler" (reagent/reactify-component (:handler params)))
           args)))

(defn DefaultRoute
  [params & args]
  (let [default (.createFactory js/React js/ReactRouter.DefaultRoute)]
    (apply default
           (js-obj
             "handler" (reagent/reactify-component (:handler params)))
           args)))

(defn NotFound
  [params & args]
  (let [default (.createFactory js/React js/ReactRouter.NotFoundRoute)]
    (apply default
           (js-obj
             "handler" (reagent/reactify-component (:handler params)))
           args)))

(defn Redirect
  [params & args]
  (let [default (.createFactory js/React js/ReactRouter.Redirect)]
    (apply default
           (js-obj
             "from" (:from params)
             "to"   (:to params)
             )
           args)))

(defn- router-cbk
  [elem handler state]
  (js/React.render (js/React.createElement handler (js-obj "params" (.-params state))) elem))

(defn run-router [elem routes]
  "receives the dom element to append the router tree to and the Routes component"
  (.run js/ReactRouter routes (partial router-cbk elem)))

(comment
  "NOT API"
  "this would be ideal but doesn't work because reactify-component adds a wrapper layer
  that conflicts with react-router"
  (defn routes[]
    [Route {:name "app" :path "/" :handler app-page}
     [Route {:name "about" :path "about" :handler about-page}]])
)

(comment
  "CURRENT API"
  "this is how you define routes for now
  every handler is a reagent component"
  (def routes
    (Route {:name "app" :path "/" :handler app-page}
      (Route {:name "about" :path "about" :handler about-page}
        (Route {:name "nested" :path ":post_id" :handler nested-page}
          (Route {:name "deeply-nested" :path ":comment_id" :handler deeply-nested-page})))
      (DefaultRoute {:handler default-page-meta})))

  )
