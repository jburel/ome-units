#!/usr/bin/env python

from base import add_si
from base import print_conversions

from sympy.physics.units import giga
from sympy.physics.units import milli
from sympy.physics.units import nano
from sympy.physics.units import peta

from sympy import symbols as symbols
from sympy import Eq
from sympy import tan


NAME = "LENGTH"

m, ang, ua, ly, pc, thou, li, inch, ft, yd, mi, pt, pixel, frame = \
    symbols("m ang ua ly pc thou li in ft yd mi pt pixel frame")

degree = symbols("degree")

equations = [

    Eq(ang, 10**10 * m),

    Eq(m, 149597870700 * ua),
    Eq(m, 9460730472580800 * ly),
    Eq(m, 3.0856776 * 10**16 * pc),  # approx

    Eq(pc, ua / (tan(degree/3600))),

    Eq(inch, 39.3701 * m),
    Eq(thou, inch / 1000),
    Eq(li, inch * 12),
    Eq(ft, inch / 12),
    Eq(yd, ft / 3),
    Eq(mi, yd / 1760),
    Eq(pt, inch * 72),
]

units = {
    m: "METER",
    ang: "ANGSTROM",
    ua: "ASTRONOMICALUNIT",
    ly: "LIGHTYEAR",
    pc: "PARSEC",
    thou: "THOU",
    li: "LINE",
    inch: "INCH",
    ft: "FOOT",
    yd: "YARD",
    mi: "MILE",
    pt: "POINT",
    pixel: "PIXEL",
    frame: "REFERENCEFRAME",
}

add_si(m, "METER", units, equations)

if __name__ == "__main__":
    import sys
    print_conversions(sys.modules[__name__])
