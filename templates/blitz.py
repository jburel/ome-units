#!/usr/bin/env python
# -*- coding: utf-8 -*-

#
# Copyright (C) 2014 Glencoe Software, Inc. All Rights Reserved.
# Use is subject to license terms supplied in LICENSE.txt
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

"""
Code-generated omero.model.${name} implementation,
based on omero.model.PermissionsI
"""


import Ice
import IceImport
IceImport.load("omero_model_${name}_ice")
_omero = Ice.openModule("omero")
_omero_model = Ice.openModule("omero.model")
__name__ = "omero.model"

from omero_model_UnitBase import UnitBase
from omero.model.enums import Units${name}

from omero.conversions import Add  # nopep8
from omero.conversions import Int  # nopep8
from omero.conversions import Mul  # nopep8
from omero.conversions import Pow  # nopep8
from omero.conversions import Rat  # nopep8
from omero.conversions import Sym  # nopep8


class ${name}I(_omero_model.${name}, UnitBase):

    UNIT_VALUES = sorted(Units${name}._enumerators.values())
    CONVERSIONS = dict()
    for val in UNIT_VALUES:
        CONVERSIONS[val] = dict()
{% for cfrom in sorted(equations) %}\
{% for cto, equation in sorted(equations.get(cfrom, {}).items()) %}\
{% if cfrom != cto %}\
    CONVERSIONS[Units${name}.${cfrom}][Units${name}.${cto}] = \\
        ${equation}  # nopep8
{% end %}\
{% end %}\
{% end %}\
    del val

    SYMBOLS = dict()
{% for x in sorted(items) %}\
    SYMBOLS["${x.CODE}"] = "${x.SYMBOL}"
{% end %}\

    def __init__(self, value=None, unit=None):
        _omero_model.${name}.__init__(self)

        if unit is None:
            target = None
        elif isinstance(unit, Units${name}):
            target = unit
        elif isinstance(unit, (str, unicode)):
            target = getattr(Units${name}, unit)
        else:
            raise Exception("Unknown unit: %s (%s)" % (
                unit, type(unit)
            ))

        if isinstance(value, _omero_model.${name}I):
            # This is a copy-constructor call.

            source = value.getUnit()

            if target is None:
                raise Exception("Null target unit")
            if source is None:
                raise Exception("Null source unit")

            if target == source:
                self.setValue(value.getValue())
                self.setUnit(source)
            else:
                c = self.CONVERSIONS.get(source).get(target)
                if c is None:
                    t = (value.getValue(), source, target)
                    msg = "%s %s cannot be converted to %s" % t
                    raise Exception(msg)
                self.setValue(c(value.getValue()))
                self.setUnit(target)
        else:
            self.setValue(value)
            self.setUnit(target)

    def getUnit(self, current=None):
        return self._unit

    def getValue(self, current=None):
        return self._value

    def getSymbol(self, current=None):
        return self.SYMBOLS.get(str(self.getUnit()))

    @staticmethod
    def lookupSymbol(unit):
        return ${name}I.SYMBOLS.get(str(unit))

    def setUnit(self, unit, current=None):
        self._unit = unit

    def setValue(self, value, current=None):
        self._value = value

    def __str__(self):
        return self._base_string(self.getValue(), self.getUnit())

_omero_model.${name}I = ${name}I\
