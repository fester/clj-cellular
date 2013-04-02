(ns cellular.core
  (:use seesaw.core seesaw.graphics seesaw.color seesaw.border)
  (:import 
   (java.awt Color Dimension Frame)))


;; Field dimensions
(def field-width 100)
(def field-height 100)

(def cell-pixel-size 3)

(def game-field (atom #{[21 22] [22 21] [22 22] [22 23] [23 21]}))

(defn neighbours [[x y]]
  "Returns a sequence of a given point's neighbours considering field borders"
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not= 0 dx dy)]
    [(+ x dx) (+ y dy)]))

  ;; (let [coords (for [dx [-1 0 1]
  ;;                    dy [-1 0 1]
  ;;                    :when (not= 0 dx dy)]
  ;;                [(+ x dx) (+ y dy)])]
  ;;   (filter (fn [[x y]]
  ;;             (and (>= x 0)
  ;;                  (>= y 0)
  ;;                  (< x field-width)
  ;;                  (< y field-height)))
  ;;           coords)))

(defn alive? [n cell]
  "Returns true if there's a life at given cell with n alive neighbours"
  (or (= n 3)
       (and (= n 2) cell)))

(defn next-step [field]
  "Accepts a set of vectors field where each element represents a single alive cell on the field"
  (set (for [[pos n] (frequencies (mapcat neighbours field))
             :when (alive? n (field pos))]
         pos)))

(defn paint-field [w g]
  "Paint function should takes a widget (ignored) and graphics element as arguments"
  (doseq [[x y] @game-field]
    (let [cx (* x cell-pixel-size)
          cy (* y cell-pixel-size)
          shape (circle cx cy cell-pixel-size)]
      (draw g shape (style :background :blue))))
  (swap! game-field next-step))

(defn run-evolution [canvas]
  (print "Evolving")
  (letfn [(evolution []
            (Thread/sleep 50)
            (repaint! canvas)
            (recur))]
    (future (evolution))))

(defn -main [& args]
  (let [canvas (canvas :id :canvas,
                       :paint paint-field
                       :background :black
                       :border (line-border :color :green :thickness 3))]
    (invoke-later
     (-> (frame 
          :title "Hello",
          :size [(* field-height cell-pixel-size) :by (* field-height cell-pixel-size)],
          :content (border-panel :center canvas),
          :on-close :exit)
         show!))
    (run-evolution canvas)))

