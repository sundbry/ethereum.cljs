(ns http.server
  (:require
    [clojure.java.io :as io]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [compojure.handler :as handler]
    [ring.adapter.jetty :as jetty]
    [ring.util.response :as response]))

(defonce httpd (atom nil))

(defroutes main-routes
  (ANY "/" [] (slurp (io/resource "public/index.html")))
  (route/resources "/") ; Route static resources
  (route/not-found "Not found!"))

(defn wrap-CORS [handler]
  (fn [req]
    (-> req
        handler
        (response/header "Access-Control-Allow-Origin" "*"))))

(defn make-handler [routes]
  (-> routes
      handler/site
      wrap-CORS))

(defn start [httpd]
  (when-not httpd
    (let [req-handler (make-handler main-routes)]
      (jetty/run-jetty req-handler {:port 8081
                                    :join? false}))))


(defn stop [httpd]
  (when httpd
    (.stop httpd)
    nil))
  
(defn -main [& args]
  (swap! httpd start))

