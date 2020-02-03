(ns crux-app.core
  (:require [crux.api :as crux])
  (:gen-class))

(defn crux []
  (crux/start-node
    {:crux.node/topology :crux.standalone/topology
     :crux.node/kv-store "crux.kv.membd/kv"
     :crux.standalone/event-log-dir "data/eventlog-1"
     :crux.kv/db-dir "data/db-dir"
     :crux.standalone/event-log-kv-store "crux.kv.memdb/kv"}))
(crux)
(defn manifest []
  {:crux.db/id :manifest
   :pilot-name "Lorenzo"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})
(manifest)
(crux/submit-tx crux [[:crux.tx/put manifest]])
(crux/entity (crux/db crux) :manifest)

(crux/submit-tx crux
  [[:crux.tx/put
    {:crux.db/id :commodity/Pu
     :common-name "Plutonium"
     :type :element/metal
     :density 19.816
     :radioactive true}]
   [:crux.tx/put
     {:crux.db/id :commodity/N
      :common-name "Nitrogen"
      :type :element/gas
      :density 1.2506
      :radioactive false}]
   [:crux.tx/put
      {:crux.db/id :commodity/CH4
       :common-name "Methane"
       :type :molecule/gas
       :density 0.717
       :radioactive false}]])


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))