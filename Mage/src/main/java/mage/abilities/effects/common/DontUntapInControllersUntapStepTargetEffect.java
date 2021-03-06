/*
* Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
*    1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*    2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and should not be interpreted as representing official policies, either expressed
* or implied, of BetaSteward_at_googlemail.com.
*/

package mage.abilities.effects.common;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.Mode;
import mage.abilities.effects.ContinuousRuleModifyingEffectImpl;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.PhaseStep;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;

/**
 * 
 * @author BetaSteward_at_googlemail.com
 */
public class DontUntapInControllersUntapStepTargetEffect extends ContinuousRuleModifyingEffectImpl {

    public DontUntapInControllersUntapStepTargetEffect(Duration duration) {
        super(duration, Outcome.Detriment);
    }

    public DontUntapInControllersUntapStepTargetEffect(final DontUntapInControllersUntapStepTargetEffect effect) {
        super(effect);
    }

    @Override
    public DontUntapInControllersUntapStepTargetEffect copy() {
        return new DontUntapInControllersUntapStepTargetEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return false;
    }

    @Override
    public String getInfoMessage(Ability source, GameEvent event, Game game) {
        MageObject mageObject = game.getObject(source.getSourceId());
        Permanent permanentToUntap = game.getPermanent((event.getTargetId()));
        if (permanentToUntap != null && mageObject != null) {
            return permanentToUntap.getLogName() + " doesn't untap (" + mageObject.getLogName() + ")";
        }
        return null;
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.UNTAP;
    }
    
    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (PhaseStep.UNTAP.equals(game.getTurn().getStepType())) {
            for (UUID targetId : targetPointer.getTargets(game, source)) {
                if (event.getTargetId().equals(targetId)) {
                    Permanent permanent = game.getPermanent(targetId);
                    if (permanent != null && game.getActivePlayerId().equals(permanent.getControllerId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getText(Mode mode) {
            if (staticText != null) {
                return staticText;
            }
            return "Target " + mode.getTargets().get(0).getTargetName()
                    + " doesn't untap during its controller's untap step" + (getDuration().toString().isEmpty() ? "":" " + getDuration());
    }

}
