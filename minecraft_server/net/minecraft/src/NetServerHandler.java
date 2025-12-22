package net.minecraft.src;
 

import java.io.PrintStream;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class NetServerHandler extends NetHandler
    implements ICommandListener
{

    public NetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayerMP entityplayermp)
    {
        field_18_c = false;
        field_9006_j = true;
        field_10_k = new HashMap();
        mcServer = minecraftserver;
        netManager = networkmanager;
        networkmanager.setNetHandler(this);
        playerEntity = entityplayermp;
        entityplayermp.field_20908_a = this;
    }

    public void func_42_a()
    {
        field_22003_h = false;
        netManager.processReadPackets();
        if(field_15_f - field_22004_g > 20)
        {
            sendPacket(new Packet0KeepAlive());
        }
    }

    public void func_43_c(String s)
    {
        sendPacket(new Packet255KickDisconnect(s));
        netManager.serverShutdown();
        mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat((new StringBuilder()).append("\247e").append(playerEntity.username).append(" left the game.").toString()));
        mcServer.configManager.playerLoggedOut(playerEntity);
        field_18_c = true;
    }

    public void func_22001_a(Packet27 packet27)
    {
        playerEntity.func_22069_a(packet27.func_22031_c(), packet27.func_22028_e(), packet27.func_22032_g(), packet27.func_22030_h(), packet27.func_22029_d(), packet27.func_22033_f());
    }

    public void handleFlying(Packet10Flying packet10flying)
    {
        field_22003_h = true;
        if(!field_9006_j)
        {
            double d = packet10flying.yPosition - field_9008_h;
            if(packet10flying.xPosition == field_9009_g && d * d < 0.01D && packet10flying.zPosition == field_9007_i)
            {
                field_9006_j = true;
            }
        }
        if(field_9006_j)
        {
            if(playerEntity.ridingEntity != null)
            {
                float f = playerEntity.rotationYaw;
                float f1 = playerEntity.rotationPitch;
                playerEntity.ridingEntity.updateRiderPosition();
                double d2 = playerEntity.posX;
                double d4 = playerEntity.posY;
                double d6 = playerEntity.posZ;
                double d8 = 0.0D;
                double d9 = 0.0D;
                if(packet10flying.rotating)
                {
                    f = packet10flying.yaw;
                    f1 = packet10flying.pitch;
                }
                if(packet10flying.moving && packet10flying.yPosition == -999D && packet10flying.stance == -999D)
                {
                    d8 = packet10flying.xPosition;
                    d9 = packet10flying.zPosition;
                }
                playerEntity.onGround = packet10flying.onGround;
                playerEntity.func_22070_a(true);
                playerEntity.moveEntity(d8, 0.0D, d9);
                playerEntity.setPositionAndRotation(d2, d4, d6, f, f1);
                playerEntity.motionX = d8;
                playerEntity.motionZ = d9;
                if(playerEntity.ridingEntity != null)
                {
                    mcServer.worldMngr.func_12017_b(playerEntity.ridingEntity, true);
                }
                if(playerEntity.ridingEntity != null)
                {
                    playerEntity.ridingEntity.updateRiderPosition();
                }
                mcServer.configManager.func_613_b(playerEntity);
                field_9009_g = playerEntity.posX;
                field_9008_h = playerEntity.posY;
                field_9007_i = playerEntity.posZ;
                mcServer.worldMngr.updateEntity(playerEntity);
                return;
            }
            double d1 = playerEntity.posY;
            field_9009_g = playerEntity.posX;
            field_9008_h = playerEntity.posY;
            field_9007_i = playerEntity.posZ;
            double d3 = playerEntity.posX;
            double d5 = playerEntity.posY;
            double d7 = playerEntity.posZ;
            float f2 = playerEntity.rotationYaw;
            float f3 = playerEntity.rotationPitch;
            if(packet10flying.moving && packet10flying.yPosition == -999D && packet10flying.stance == -999D)
            {
                packet10flying.moving = false;
            }
            if(packet10flying.moving)
            {
                d3 = packet10flying.xPosition;
                d5 = packet10flying.yPosition;
                d7 = packet10flying.zPosition;
                double d10 = packet10flying.stance - packet10flying.yPosition;
                if(d10 > 1.6499999999999999D || d10 < 0.10000000000000001D)
                {
                    func_43_c("Illegal stance");
                    logger.warning((new StringBuilder()).append(playerEntity.username).append(" had an illegal stance: ").append(d10).toString());
                }
            }
            if(packet10flying.rotating)
            {
                f2 = packet10flying.yaw;
                f3 = packet10flying.pitch;
            }
            playerEntity.func_22070_a(true);
            playerEntity.ySize = 0.0F;
            playerEntity.setPositionAndRotation(field_9009_g, field_9008_h, field_9007_i, f2, f3);
            double d11 = d3 - playerEntity.posX;
            double d12 = d5 - playerEntity.posY;
            double d13 = d7 - playerEntity.posZ;
            float f4 = 0.0625F;
            boolean flag = mcServer.worldMngr.getCollidingBoundingBoxes(playerEntity, playerEntity.boundingBox.copy().func_694_e(f4, f4, f4)).size() == 0;
            playerEntity.moveEntity(d11, d12, d13);
            d11 = d3 - playerEntity.posX;
            d12 = d5 - playerEntity.posY;
            if(d12 > -0.5D || d12 < 0.5D)
            {
                d12 = 0.0D;
            }
            d13 = d7 - playerEntity.posZ;
            double d14 = d11 * d11 + d12 * d12 + d13 * d13;
            boolean flag1 = false;
            if(d14 > 0.0625D && !playerEntity.func_22057_E())
            {
                flag1 = true;
                logger.warning((new StringBuilder()).append(playerEntity.username).append(" moved wrongly!").toString());
                System.out.println((new StringBuilder()).append("Got position ").append(d3).append(", ").append(d5).append(", ").append(d7).toString());
                System.out.println((new StringBuilder()).append("Expected ").append(playerEntity.posX).append(", ").append(playerEntity.posY).append(", ").append(playerEntity.posZ).toString());
            }
            playerEntity.setPositionAndRotation(d3, d5, d7, f2, f3);
            boolean flag2 = mcServer.worldMngr.getCollidingBoundingBoxes(playerEntity, playerEntity.boundingBox.copy().func_694_e(f4, f4, f4)).size() == 0;
            if(flag && (flag1 || !flag2) && !playerEntity.func_22057_E())
            {
                func_41_a(field_9009_g, field_9008_h, field_9007_i, f2, f3);
                return;
            }
            playerEntity.onGround = packet10flying.onGround;
            mcServer.configManager.func_613_b(playerEntity);
            playerEntity.func_9153_b(playerEntity.posY - d1, packet10flying.onGround);
        }
    }

    public void func_41_a(double d, double d1, double d2, float f, 
            float f1)
    {
        field_9006_j = false;
        field_9009_g = d;
        field_9008_h = d1;
        field_9007_i = d2;
        playerEntity.setPositionAndRotation(d, d1, d2, f, f1);
        playerEntity.field_20908_a.sendPacket(new Packet13PlayerLookMove(d, d1 + 1.6200000047683716D, d1, d2, f, f1, false));
    }

    public void handleBlockDig(Packet14BlockDig packet14blockdig)
    {
        if(packet14blockdig.status == 4)
        {
            playerEntity.func_161_a();
            return;
        }
        boolean flag = mcServer.worldMngr.field_819_z = mcServer.configManager.isOp(playerEntity.username);
        boolean flag1 = false;
        if(packet14blockdig.status == 0)
        {
            flag1 = true;
        }
        if(packet14blockdig.status == 2)
        {
            flag1 = true;
        }
        int i = packet14blockdig.xPosition;
        int j = packet14blockdig.yPosition;
        int k = packet14blockdig.zPosition;
        if(flag1)
        {
            double d = playerEntity.posX - ((double)i + 0.5D);
            double d1 = playerEntity.posY - ((double)j + 0.5D);
            double d3 = playerEntity.posZ - ((double)k + 0.5D);
            double d5 = d * d + d1 * d1 + d3 * d3;
            if(d5 > 36D)
            {
                return;
            }
        }
        ChunkCoordinates chunkcoordinates = mcServer.worldMngr.func_22078_l();
        int l = (int)MathHelper.abs(i - chunkcoordinates.field_22216_a);
        int i1 = (int)MathHelper.abs(k - chunkcoordinates.field_528_b);
        if(l > i1)
        {
            i1 = l;
        }
        if(packet14blockdig.status == 0)
        {
            if(i1 > 16 || flag)
            {
                playerEntity.field_425_ad.func_324_a(i, j, k);
            }
        } else
        if(packet14blockdig.status == 2)
        {
            playerEntity.field_425_ad.func_22045_b(i, j, k);
        } else
        if(packet14blockdig.status == 3)
        {
            double d2 = playerEntity.posX - ((double)i + 0.5D);
            double d4 = playerEntity.posY - ((double)j + 0.5D);
            double d6 = playerEntity.posZ - ((double)k + 0.5D);
            double d7 = d2 * d2 + d4 * d4 + d6 * d6;
            if(d7 < 256D)
            {
                playerEntity.field_20908_a.sendPacket(new Packet53BlockChange(i, j, k, mcServer.worldMngr));
            }
        }
        mcServer.worldMngr.field_819_z = false;
    }

    public void handlePlace(Packet15Place packet15place)
    {
        ItemStack itemstack = playerEntity.inventory.getCurrentItem();
        boolean flag = mcServer.worldMngr.field_819_z = mcServer.configManager.isOp(playerEntity.username);
        if(packet15place.direction == 255)
        {
            if(itemstack == null)
            {
                return;
            }
            playerEntity.field_425_ad.func_6154_a(playerEntity, mcServer.worldMngr, itemstack);
        } else
        {
            int i = packet15place.xPosition;
            int j = packet15place.yPosition;
            int k = packet15place.zPosition;
            int l = packet15place.direction;
            ChunkCoordinates chunkcoordinates = mcServer.worldMngr.func_22078_l();
            int i1 = (int)MathHelper.abs(i - chunkcoordinates.field_22216_a);
            int j1 = (int)MathHelper.abs(k - chunkcoordinates.field_528_b);
            if(i1 > j1)
            {
                j1 = i1;
            }
            if(j1 > 16 || flag)
            {
                playerEntity.field_425_ad.func_327_a(playerEntity, mcServer.worldMngr, itemstack, i, j, k, l);
            }
            playerEntity.field_20908_a.sendPacket(new Packet53BlockChange(i, j, k, mcServer.worldMngr));
            if(l == 0)
            {
                j--;
            }
            if(l == 1)
            {
                j++;
            }
            if(l == 2)
            {
                k--;
            }
            if(l == 3)
            {
                k++;
            }
            if(l == 4)
            {
                i--;
            }
            if(l == 5)
            {
                i++;
            }
            playerEntity.field_20908_a.sendPacket(new Packet53BlockChange(i, j, k, mcServer.worldMngr));
        }
        if(itemstack != null && itemstack.stackSize == 0)
        {
            playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = null;
        }
        playerEntity.field_20064_am = true;
        playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = ItemStack.func_20117_a(playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem]);
        Slot slot = playerEntity.craftingInventory.func_20127_a(playerEntity.inventory, playerEntity.inventory.currentItem);
        playerEntity.craftingInventory.func_20125_a();
        playerEntity.field_20064_am = false;
        if(!ItemStack.areItemStacksEqual(playerEntity.inventory.getCurrentItem(), packet15place.itemStack))
        {
            sendPacket(new Packet103(playerEntity.craftingInventory.windowId, slot.field_20100_c, playerEntity.inventory.getCurrentItem()));
        }
        mcServer.worldMngr.field_819_z = false;
    }

    public void handleErrorMessage(String s, Object aobj[])
    {
        logger.info((new StringBuilder()).append(playerEntity.username).append(" lost connection: ").append(s).toString());
        mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat((new StringBuilder()).append("\247e").append(playerEntity.username).append(" left the game.").toString()));
        mcServer.configManager.playerLoggedOut(playerEntity);
        field_18_c = true;
    }

    public void registerPacket(Packet packet)
    {
        logger.warning((new StringBuilder()).append(getClass()).append(" wasn't prepared to deal with a ").append(packet.getClass()).toString());
        func_43_c("Protocol error, unexpected packet");
    }

    public void sendPacket(Packet packet)
    {
        netManager.addToSendQueue(packet);
        field_22004_g = field_15_f;
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch packet16blockitemswitch)
    {
        playerEntity.inventory.currentItem = packet16blockitemswitch.id;
    }

    public void handleChat(Packet3Chat packet3chat)
    {
        String s = packet3chat.message;
        if(s.length() > 100)
        {
            func_43_c("Chat message too long");
            return;
        }
        s = s.trim();
        for(int i = 0; i < s.length(); i++)
        {
            if(FontAllowedCharacters.field_20162_a.indexOf(s.charAt(i)) < 0)
            {
                func_43_c("Illegal characters in chat");
                return;
            }
        }

        if(s.startsWith("/"))
        {
            func_4010_d(s);
        } else
        {
            s = (new StringBuilder()).append("<").append(playerEntity.username).append("> ").append(s).toString();
            logger.info(s);
            mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(s));
        }
    }

    private void func_4010_d(String s)
    {
        if(s.toLowerCase().startsWith("/me "))
        {
            s = (new StringBuilder()).append("* ").append(playerEntity.username).append(" ").append(s.substring(s.indexOf(" ")).trim()).toString();
            logger.info(s);
            mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(s));
        } else
        if(s.toLowerCase().startsWith("/kill"))
        {
            playerEntity.attackEntityFrom(null, 1000);
        } else
        if(s.toLowerCase().startsWith("/tell "))
        {
            String as[] = s.split(" ");
            if(as.length >= 3)
            {
                s = s.substring(s.indexOf(" ")).trim();
                s = s.substring(s.indexOf(" ")).trim();
                s = (new StringBuilder()).append("\2477").append(playerEntity.username).append(" whispers ").append(s).toString();
                logger.info((new StringBuilder()).append(s).append(" to ").append(as[1]).toString());
                if(!mcServer.configManager.sendPacketToPlayer(as[1], new Packet3Chat(s)))
                {
                    sendPacket(new Packet3Chat("\247cThere's no player by that name online."));
                }
            }
        } else
        if(mcServer.configManager.isOp(playerEntity.username))
        {
            String s1 = s.substring(1);
            logger.info((new StringBuilder()).append(playerEntity.username).append(" issued server command: ").append(s1).toString());
            mcServer.addCommand(s1, this);
        } else
        {
            String s2 = s.substring(1);
            logger.info((new StringBuilder()).append(playerEntity.username).append(" tried command: ").append(s2).toString());
        }
    }

    public void handleArmAnimation(Packet18ArmAnimation packet18armanimation)
    {
        if(packet18armanimation.animate == 1)
        {
            playerEntity.swingItem();
        }
    }

    public void func_21001_a(Packet19 packet19)
    {
        if(packet19.state == 1)
        {
            playerEntity.func_21043_b(true);
        } else
        if(packet19.state == 2)
        {
            playerEntity.func_21043_b(false);
        } else
        if(packet19.state == 3)
        {
            playerEntity.func_22062_a(false, true);
            field_9006_j = false;
        }
    }

    public void handleKickDisconnect(Packet255KickDisconnect packet255kickdisconnect)
    {
        netManager.networkShutdown("disconnect.quitting", new Object[0]);
    }

    public int func_38_b()
    {
        return netManager.getNumChunkDataPackets();
    }

    public void log(String s)
    {
        sendPacket(new Packet3Chat((new StringBuilder()).append("\2477").append(s).toString()));
    }

    public String getUsername()
    {
        return playerEntity.username;
    }

    public void func_6006_a(Packet7 packet7)
    {
        Entity entity = mcServer.worldMngr.func_6158_a(packet7.targetEntity);
        if(entity != null && playerEntity.canEntityBeSeen(entity) && playerEntity.getDistanceToEntity(entity) < 4F)
        {
            if(packet7.isLeftClick == 0)
            {
                playerEntity.useCurrentItemOnEntity(entity);
            } else
            if(packet7.isLeftClick == 1)
            {
                playerEntity.attackTargetEntityWithCurrentItem(entity);
            }
        }
    }

    public void func_9002_a(Packet9 packet9)
    {
        if(playerEntity.health > 0)
        {
            return;
        } else
        {
            playerEntity = mcServer.configManager.func_9242_d(playerEntity);
            return;
        }
    }

    public void func_20006_a(Packet101 packet101)
    {
        playerEntity.func_20059_K();
    }

    public void func_20007_a(Packet102 packet102)
    {
        if(playerEntity.craftingInventory.windowId == packet102.window_Id && playerEntity.craftingInventory.func_20124_c(playerEntity))
        {
            ItemStack itemstack = playerEntity.craftingInventory.func_20123_a(packet102.inventorySlot, packet102.mouseClick, playerEntity);
            if(ItemStack.areItemStacksEqual(packet102.itemStack, itemstack))
            {
                playerEntity.field_20908_a.sendPacket(new Packet106(packet102.window_Id, packet102.action, true));
                playerEntity.field_20064_am = true;
                playerEntity.craftingInventory.func_20125_a();
                playerEntity.func_20058_J();
                playerEntity.field_20064_am = false;
            } else
            {
                field_10_k.put(Integer.valueOf(playerEntity.craftingInventory.windowId), Short.valueOf(packet102.action));
                playerEntity.field_20908_a.sendPacket(new Packet106(packet102.window_Id, packet102.action, false));
                playerEntity.craftingInventory.func_20129_a(playerEntity, false);
                ArrayList arraylist = new ArrayList();
                for(int i = 0; i < playerEntity.craftingInventory.field_20135_e.size(); i++)
                {
                    arraylist.add(((Slot)playerEntity.craftingInventory.field_20135_e.get(i)).getStack());
                }

                playerEntity.func_20054_a(playerEntity.craftingInventory, arraylist);
            }
        }
    }

    public void func_20008_a(Packet106 packet106)
    {
        Short short1 = (Short)field_10_k.get(Integer.valueOf(playerEntity.craftingInventory.windowId));
        if(short1 != null && packet106.field_20033_b == short1.shortValue() && playerEntity.craftingInventory.windowId == packet106.windowId && !playerEntity.craftingInventory.func_20124_c(playerEntity))
        {
            playerEntity.craftingInventory.func_20129_a(playerEntity, true);
        }
    }

    public void func_20005_a(Packet130 packet130)
    {
        if(mcServer.worldMngr.blockExists(packet130.xPosition, packet130.yPosition, packet130.zPosition))
        {
            TileEntity tileentity = mcServer.worldMngr.getBlockTileEntity(packet130.xPosition, packet130.yPosition, packet130.zPosition);
            for(int i = 0; i < 4; i++)
            {
                boolean flag = true;
                if(packet130.signLines[i].length() > 15)
                {
                    flag = false;
                } else
                {
                    for(int l = 0; l < packet130.signLines[i].length(); l++)
                    {
                        if(FontAllowedCharacters.field_20162_a.indexOf(packet130.signLines[i].charAt(l)) < 0)
                        {
                            flag = false;
                        }
                    }

                }
                if(!flag)
                {
                    packet130.signLines[i] = "!?";
                }
            }

            if(tileentity instanceof TileEntitySign)
            {
                int j = packet130.xPosition;
                int k = packet130.yPosition;
                int i1 = packet130.zPosition;
                TileEntitySign tileentitysign = (TileEntitySign)tileentity;
                for(int j1 = 0; j1 < 4; j1++)
                {
                    tileentitysign.signText[j1] = packet130.signLines[j1];
                }

                tileentitysign.onInventoryChanged();
                mcServer.worldMngr.markBlockNeedsUpdate(j, k, i1);
            }
        }
    }

    public static Logger logger = Logger.getLogger("Minecraft");
    public NetworkManager netManager;
    public boolean field_18_c;
    private MinecraftServer mcServer;
    private EntityPlayerMP playerEntity;
    private int field_15_f;
    private int field_22004_g;
    private boolean field_22003_h;
    private double field_9009_g;
    private double field_9008_h;
    private double field_9007_i;
    private boolean field_9006_j;
    private Map field_10_k;

}
