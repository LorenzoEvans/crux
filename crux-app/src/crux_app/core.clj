(ns crux-app.core
  (:require [crux.api :as crux])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def crux
  (crux/start-node
    {:crux.node/topology :crux.standalone/topology
     :crux.node/kv-store "crux.kv.membd/kv"
     :crux.standalone/event-log-dir "data/db-dir"
     :crux.standalone/event-log-dir "crux.kv.memdb/kv"}))

(def manifest
  {:crux.db/id :manifest
   :pilot-name "Lorenzo"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})

(crux/submit-tx crux [[:crux.tx/put manifest]])
(crux/entity (crux/db crux) :manifest)