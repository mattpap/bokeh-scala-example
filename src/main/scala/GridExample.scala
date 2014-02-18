package org.continuumio.bokeh.example

import org.continuumio.bokeh._
import DefaultTools._

import breeze.linalg.linspace
import breeze.numerics.{sin,cos,tan}
import math.{Pi=>pi}

object GridExample extends App {
    val x = linspace(-2*pi, 2*pi, 1000)

    val source = new ColumnDataSource()
        .addColumn('x,  x)
        .addColumn('y1, sin(x))
        .addColumn('y2, cos(x))
        .addColumn('y3, tan(x))
        .addColumn('y4, sin(x) :* cos(x))

    def make_plot(title: String, xname: Symbol, yname: Symbol, line_color: Color,
            _xdr: Option[Range]=None, _ydr: Option[Range]=None) = {

        val xdr = _xdr getOrElse new DataRange1d().sources(source.columns(xname) :: Nil)
        val ydr = _ydr getOrElse new DataRange1d().sources(source.columns(yname) :: Nil)

        val plot = new Plot().title(title).x_range(xdr).y_range(ydr).data_sources(source :: Nil)

        val xaxis = new LinearAxis().plot(plot).dimension(0).location(Location.Bottom)
        val yaxis = new LinearAxis().plot(plot).dimension(1).location(Location.Left)

        val renderer = new Glyph()
            .data_source(source)
            .xdata_range(xdr)
            .ydata_range(ydr)
            .glyph(new Line().x(xname).y(yname).line_color(line_color))

        plot.renderers := List(xaxis, yaxis, renderer)
        plot.tools := Pan|WheelZoom

        plot
    }

    val plot1 = make_plot("sin(x)",        'x, 'y1, Color.Blue)
    val plot2 = make_plot("cos(x)",        'x, 'y2, Color.Red, _xdr=plot1.x_range.valueOpt)
    val plot3 = make_plot("tan(x)",        'x, 'y3, Color.Green)
    val plot4 = make_plot("sin(x)*cos(x)", 'x, 'y4, Color.Black)

    val children = List(List(plot1, plot2), List(plot3, plot4))
    val grid = new GridPlot().children(children)

    val session = new HTMLFileSession("grid_example.html")
    session.save(grid)
    println(s"Wrote ${session.file}. Open ${session.url} in a web browser.")
}
