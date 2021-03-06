/*
 * Copyright (C) 2014 University of Dundee & Open Microscopy Environment
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

#ifndef OMERO_MODEL_${name.upper()}I_H
#define OMERO_MODEL_${name.upper()}I_H

#include <omero/IceNoWarnPush.h>
#include <omero/model/${name}.h>
#include <omero/model/Units.h>
#include <omero/IceNoWarnPop.h>

#include <omero/conversions.h>

#ifndef OMERO_CLIENT
#   ifdef OMERO_CLIENT_EXPORTS
#       define OMERO_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define OMERO_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace omero {
  namespace model {
    class ${name}I;
  }
}

namespace IceInternal {
  OMERO_CLIENT ::Ice::Object* upCast(::omero::model::${name}I*);
}

namespace omero {
  namespace model {

    typedef IceInternal::Handle<${name}I> ${name}IPtr;

    class OMERO_CLIENT ${name}I : virtual public ${name} {

    protected:
        virtual ~${name}I(); // protected as outlined in Ice docs.
        static std::map<enums::Units${name},
            std::map<enums::Units${name},
                omero::conversions::ConversionPtr> > CONVERSIONS;
        static std::map<enums::Units${name}, std::string> SYMBOLS;

    public:

        static std::string lookupSymbol(enums::Units${name} unit) {
            return SYMBOLS[unit];
        }

        ${name}I();

        ${name}I(const double& value, const enums::Units${name}& unit);

        // Conversion constructor
        ${name}I(const ${name}Ptr& value, const enums::Units${name}& target);

        virtual Ice::Double getValue(
                const Ice::Current& current = Ice::Current());

        virtual void setValue(
                Ice::Double value,
                const Ice::Current& current = Ice::Current());

        virtual enums::Units${name} getUnit(
                const Ice::Current& current = Ice::Current());

        virtual void setUnit(
                enums::Units${name} unit,
                const Ice::Current& current = Ice::Current());

        virtual std::string getSymbol(
                const Ice::Current& current = Ice::Current());

        virtual ${name}Ptr copy(
                const Ice::Current& = Ice::Current());

    };
  }
}
#endif // OMERO_MODEL_${name.upper()}I_H
