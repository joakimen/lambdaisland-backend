(ns core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as ring-defaults]
            [compojure.core :as c]
            [hiccup2.core :as h]))

(defonce server (atom nil))

(defn home-view []
  [:html [:body [:h1 "Welcome home!"]]])

(c/defroutes app-routes
  (c/GET "/" []
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str (h/html (home-view)))})
  (c/GET "/:name" [name]
    {:status 200
     :body  (str "Hello " name "!")}))

(defn handler [req]
  (app-routes req))

(defn start-jetty! [& _]
  (reset! server (jetty/run-jetty
                  (ring-defaults/wrap-defaults #'handler ring-defaults/site-defaults)
                  {:join? false
                   :port 3428})))

(defn stop-jetty! []
  (.stop @server)
  (reset! server nil))

(defn -main [& _]
  (reset! server (start-jetty!)))

(comment

  (start-jetty!)
  (stop-jetty!)
  ;;
  )
