(ns cellular.core
  (:use seesaw.core seesaw.graphics seesaw.color)
  (:import 
   (java.awt Color Dimension Frame)))


;; Field dimensions
(def field-width 100)
(def field-height 100)

;; Reference to a field hash
(def field (atom {}))

;; List of coordinates offsets to gather a cell's neighbours
(def neighbours-offsets (for [dx [-1 0 1] dy [-1 0 1] :when (not= dx dy 0)] [dx dy]))

(defn in-field [cell]
  "Returns true if cell is within the bounds of a field and false otherwise"
  (let [[x y] cell]
    (and (>= x 0) (>= y 0)
         (< x field-width) (< y field-height))))

(defn field-at [cell]
  "Returns a current value of a cell or nil"
  (cell @field))

(defn neighbouring-cells [cell]
  "Returns a list of coordinates of a neighbouring cells"
  (letfn [(add-coords [x y] (map + x y))]
    (filter in-field
            (map #(add-coords cell %) neighbours-offsets))))

(defn count-alive-neighbours [cell]
  "Counts number of alive neighbours of a given cell"
  (let [ns (neighbouring-cells cell)]
    (count (filter (complement nil?) (map field-at ns)))))


(defn cell-expires? [cell]
  (if-let [state (cell @field)]
    (<= state 0)))

;; 0. Allocate new cells
;; 1. Generate new cells
;; 2. Expire old cells
;; 3. Swap fields values

;; (defn run-simulation-step []
;;   (let [new-field (atom {})]
;;     (doseq [x (range field-width)
;;             y (range field-height)]
;;       (let [cell [x y]
;;             cnt (count-alive-neighbours cell)]
;;         (cond 
;;          (>= cnt S) (swap! new-field assoc cell C)
;;          (>

(defn paint []
  )

(defn -main [& args]
  (invoke-later
   (-> (frame :title "Hello",
              :width 500, :height 500,
              :content "Hello, Seesaw",
              :on-close :exit)
       show!)))