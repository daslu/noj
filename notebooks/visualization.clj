;; # Visualization

(ns visualization
  (:require [tablecloth.api :as tc]
            [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [scicloj.noj.v1.vis.hanami.templates :as vht]
            [scicloj.noj.v1.vis.hanami :as vis.hanami]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]
            [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [hiccup.core :as hiccup]
            [clojure2d.color :as color]
            [datasets]))

;; ## Visualizing datases with Hanami

;; Noj offers a few convenience functions to make [Hanami](https://github.com/jsa-aerial/hanami) plotting work smoothly with [Tablecloth](https://scicloj.github.io/tablecloth/) and [Kindly](https://scicloj.github.io/kindly/).

(def random-walk
  (let [n 20]
    (-> {:x (range n)
         :y (->> (repeatedly n #(- (rand) 0.5))
                 (reductions +))}
        tc/dataset)))

;; ### A simple plot

;; We can plot a Tablecloth datasete using a Hanami template:

(-> random-walk
    (vis.hanami/plot ht/point-chart
                 {:MSIZE 200}))

;; Let us look inside the resulting vega-lite space. We can see the dataset is included as CSV:

(-> random-walk
    (vis.hanami/plot ht/point-chart
                     {:MSIZE 200})
    kind/pprint)

;; ### Additional Hanami templates

;; The `scicloj.noj.v1.vis.hanami.templates` namespace add Hanami templates to Hanami's own collection.

(-> datasets/mtcars
    (vis.hanami/plot vht/boxplot-chart
                     {:X :gear
                      :XTYPE :nominal
                      :Y :mpg}))

(-> datasets/iris
    (vis.hanami/plot vht/rule-chart
                     {:X :sepal-width
                      :Y :sepal-length
                      :X2 :petal-width
                      :Y2 :petal-length
                      :OPACITY 0.2
                      :SIZE 3
                      :COLOR "species"}))

;; ### Grouped datasets

;; Grouped datasets are handled automatically with a table view.

(-> datasets/iris
    (tc/group-by [:species])
    (vis.hanami/plot vht/rule-chart
                     {:X :sepal-width
                      :Y :sepal-length
                      :X2 :petal-width
                      :Y2 :petal-length
                      :OPACITY 0.2
                      :SIZE 3}))

;; ### Layers

(-> random-walk
    (vis.hanami/layers
     {:TITLE "points and a line"}
     [(vis.hanami/plot nil
                       ht/point-chart
                       {:MSIZE 400})
      (vis.hanami/plot nil
                       ht/line-chart
                       {:MSIZE 4
                        :MCOLOR "brown"})]))

;; Alternatively:

(-> random-walk
    (vis.hanami/combined-plot
     ht/layer-chart
     {:TITLE "points and a line"}
     :LAYER [[ht/point-chart
              {:MSIZE 400}]
             [ht/line-chart
              {:MSIZE 4
               :MCOLOR "brown"}]]))

;; ### Concatenation

;; Vertical

(-> random-walk
    (vis.hanami/vconcat
     {}
     [(vis.hanami/plot nil
                       ht/point-chart
                       {:MSIZE 400
                        :HEIGHT 100
                        :WIDTH 100})
      (vis.hanami/plot nil
                       ht/line-chart
                       {:MSIZE 4
                        :MCOLOR "brown"
                        :HEIGHT 100
                        :WIDTH 100})]))

;; Alternatively:

(-> random-walk
    (vis.hanami/combined-plot
     ht/vconcat-chart
     {:HEIGHT 100
      :WIDTH 100}
     :VCONCAT [[ht/point-chart
                {:MSIZE 400}]
               [ht/line-chart
                {:MSIZE 4
                 :MCOLOR "brown"}]]))

;; Horizontal

(-> random-walk
    (vis.hanami/hconcat
     {}
     [(vis.hanami/plot nil
                       ht/point-chart
                       {:MSIZE 400
                        :HEIGHT 100
                        :WIDTH 100})
      (vis.hanami/plot nil
                       ht/line-chart
                       {:MSIZE 4
                        :MCOLOR "brown"
                        :HEIGHT 100
                        :WIDTH 100})]))

;; Alternatively:
(-> random-walk
    (vis.hanami/combined-plot
     ht/hconcat-chart
     {:HEIGHT 100
      :WIDTH 100}
     :HCONCAT [[ht/point-chart
                {:MSIZE 400}]
               [ht/line-chart
                {:MSIZE 4
                 :MCOLOR "brown"}]]))


:bye
