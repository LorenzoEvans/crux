(ns crux-app.core
  (:require [crux.api :as crux])
  (:gen-class))

(def crux
  (crux/start-node
       {:crux.node/topology :crux.standalone/topology
        :crux.node/kv-store "crux.kv.memdb/kv"
        :crux.standalone/event-log-dir "data/eventlog-1"
        :crux.kv/db-dir "data/db-dir"
        :crux.standalone/event-log-kv-store "crux.kv.memdb/kv"}))
(crux)
(def manifest 
  {:crux.db/id :manifest
   :pilot-name "Lorenzo"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})

(crux/submit-tx crux [[:crux.tx/put manifest]]))
(crux/entity (crux/db crux) :manifest)

(crux/submit-tx crux
  [[:crux.tx/put
    {:crux.db/id :commodity/Pu
     :common-name "Plutonium"
     :type :element/metal
     :density 19.816
     :radioactive true
     #inst "2115-02-13T18"}]
   [:crux.tx/put
     {:crux.db/id :commodity/N
      :common-name "Nitrogen"
      :type :element/gas
      :density 1.2506
      :radioactive false
      #inst "2115-02-14T18"}]
   [:crux.tx/put
      {:crux.db/id :commodity/CH4
       :common-name "Methane"
       :type :molecule/gas
       :density 0.717
       :radioactive false}
      #inst "2115-02-15T18"]])

(crux/submit-tx crux
                [[:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 21}
                  #inst "2115-02-13T18"] ;; valid-time

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 23}
                  #inst "2115-02-14T18"]

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 22.2}
                  #inst "2115-02-15T18"]

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 24}
                  #inst "2115-02-18T18"]

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 24.9}
                  #inst "2115-02-19T18"]])


(crux/submit-tx crux
                [[:crux.tx/put
                  {:crux.db/id :stock/N
                   :commod :commodity/N
                   :weight-ton 3}
                  #inst "2115-02-13T18" ;; start valid-time
                  #inst "2115-02-19T18"] ;; end valid-time

                 [:crux.tx/put
                  {:crux.db/id :stock/CH4
                   :commod :commodity/CH4
                   :weight-ton 92}
                  #inst "2115-02-15T18"
                  #inst "2115-02-19T18"]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))