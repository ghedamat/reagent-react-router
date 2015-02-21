(ns reagent-react-router-demo.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [reagent-react-router.core :refer [Route DefaultRoute Link RouteHandler Redirect NotFound run-router defroutes]]
              [goog.events :as events]
              [ajax.core :refer [GET POST]]
              [cljsjs.react :as react])
    (:import goog.History))


(defn navbar []
  [:div.navbar.navbar-inverse.navbar-fixed-top
   [:div.container
    [:div.navbar-header
     [:a.navbar-brand {:href "#/"} "quiescent"]]
    [:div.navbar-collapse.collapse
     [:ul.nav.navbar-nav
      [:li
       [Link {:to "app"}
        "home"]]
      [:li
       [Link {:to "about"}
        "about page"]]
      [:li
       [Link {:to ":post_id" :params {:post_id 12}}
        "about nested page"]]
            ]]]])

(defn timer-component []
  (let  [seconds-elapsed  (atom 0)]
    (fn  []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:div
       "Seconds Elapsed: " @seconds-elapsed])))

(defn about-page [m]
  [:div
   "about"
   [:br]
   [timer-component]
   [RouteHandler m]] )

(defn nested-page [m]
  (let [id (-> m :params (.-post_id))]
    [:div
     "nested"
     [:br]
     id
     [RouteHandler m]]))

(defn deeply-nested-page [m]
  (let [id (-> m :params (.-comment_id))]
    [:div
     "deeply nested"
     [:br]
     id]))


(defn app-page [m]
  [:div
   [navbar]
   [RouteHandler m]])

(defn alert-hello [] (js/alert "hello"))

(def click-count  (atom 0))

(def url-list  (atom []))

(defn load-gists []
    (GET  "https://api.github.com/users/ghedamat/gists"
          {:response-format :json
           :keywords? true
           :handler (fn [d]
                      (let [urls (map :url d)]
                        (reset! url-list urls)))}))

(defn default-page []
  [:div
   [:p
    "default handler content"]
   [:ul
    (for [p @url-list]
      ^{:key p} [:li p])]
   [:button {:on-click load-gists} "click me"]])

(def default-page-meta (with-meta default-page {:component-did-mount load-gists}))

(defn not-found-page []
  [:h3 "not found"])

(def r
  (defroutes [:route "/" app-page
              [:route "about" about-page
               [:route ":post_id" nested-page
                [:route ":comment_id" deeply-nested-page]]]
              [:redirect "red" "about"]
              [:not-found not-found-page]
              [:default-route default-page-meta]]))


(def routes
  (Route {:name "app" :path "/" :handler app-page}
    (Route {:name "about" :path "/about" :handler about-page}
      (Route {:name ":post_id" :path ":post_id" :handler nested-page}
        (Route {:name "deeply-nested" :path ":comment_id" :handler deeply-nested-page})))))

(js/console.log routes r)

(defn init! []
  (run-router document.body r))
