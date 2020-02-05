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

(defn easy-ingest
  "Uses crux put tx to add a vector of docs to specific node."
  [node docs]
  (crux/submit-tx node
    (vec (for [doc docs]
          [:crux.tx/put doc]))))

(defn stock-check
  [company-id item]
  {:result (crux/q (crux/db crux)
                   {:find '[name funds stock]
                    :where ['[e :company-name name]
                            '[e :credits funds]
                            ['e item 'stock]]
                    :args [{'e company-id}]})
   :item item})

(defn format-stock-check
  [{:keys [result item] :as stock-check}]
  (for [[name funds commod] result]
    (str "Name: " name ", Funds: " funds ", " item " " commod)))

(defn full-query
  [node]
  (crux/q
   (crux/db node)
   '{:find [id]
     :where [[e :crux.db/id id]]
     :full-results? true}))

(crux/submit-tx crux
  [[:crux.tx/put (assoc manifest :badges ["SETUP" "PUT"])]]

(easy-ingest crux
                [{:crux.db/id :commodity/Pu
                  :common-name "Plutonium"
                  :type :element/metal
                  :density 19.816
                  :radioactive true}
                 {:crux.db/id :commodity/N
                  :common-name "Nitrogen"
                  :type :element/gas
                  :density 1.2596
                  :radioactive false}
                 {:crux.db/id :commodity/CH4
                  :common-name "Methane"
                  :type :element/gas
                  :density 0.717
                  :radioactive false}
                 {:crux.db/id :commodity/Au
                  :common-name "Gold"
                  :type :element/metal
                  :density 19.300
                  :radioactive false}
                 {:crux.db/id :commodity/C
                  :common-name "Carbon"
                  :type :element/non-metal
                  :density 2.267
                  :radioactive false}
                 {:crux.db/id :commodity/borax
                  :common-name "Borax"
                  :IUPAC-name "Sodium tetraborate decahydrate"
                  :other-names ["Borax decahydrate" "sodium borate" "sodium tetraborate" "disodium tetraborate"]
                  :type :mineral/solid
                  :appearance "white solid"
                  :density 1.73
                  :radioactive false}])

(defn filter-type 
  [type]
  (crux/q (crux/db crux)
        {:find '[name]
         :where '[[e :type t]
                  [e :common-name name]]
         :args [{'t type}]}))
  
(defn filter-appearance
  [description]
  (crux/q (crux/db crux)
        {:find '[name IUPAC]
         :where '[[e :common-name name]
                  [e :IUPAC-name IUPAC]
                  [e :appearance appearance]]
         :args [{'appearance description}]}))
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
