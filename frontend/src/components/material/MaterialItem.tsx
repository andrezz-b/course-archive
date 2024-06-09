import { Material, MaterialVote } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";
import { Button } from "@/components/ui/button.tsx";
import { CircleChevronDown, CircleChevronUp, File, MessageSquareText } from "lucide-react";
import { cn } from "@/lib/utils.ts";
import { Link } from "react-router-dom";
import { Badge } from "@/components/ui/badge.tsx";

interface MaterialItemProps {
  material: Material;
  showCommentsLink?: boolean;
}

const MaterialItem = ({ material, showCommentsLink = true }: MaterialItemProps) => {
  const { mutate: getFile } = MaterialService.useGetFile();
  const { mutate: vote } = MaterialService.useVote();
  const openFile = () => {
    const newWindow = window.open();
    getFile(material.id, {
      onSuccess: (data) => {
        const url = window.URL.createObjectURL(data);
        if (newWindow) newWindow.location.href = url;
      },
    });
  };

  const handleVote = (voteType: MaterialVote) => {
    vote({ materialId: material.id, voteType });
  };

  return (
    <div className="flex items-center gap-2 p-4 border rounded-md my-2">
      <div className="flex flex-col items-center gap-1">
        <Button
          onClick={() => handleVote(MaterialVote.UPVOTE)}
          variant="ghost"
          className="p-1 h-auto"
        >
          <CircleChevronUp
            className={cn({ "fill-amber-600": material.currentUserVote === MaterialVote.UPVOTE })}
          />
        </Button>
        {material.voteCount}
        <Button
          onClick={() => handleVote(MaterialVote.DOWNVOTE)}
          variant="ghost"
          className="p-1 h-auto"
        >
          <CircleChevronDown
            className={cn({
              "fill-blue-500": material.currentUserVote === MaterialVote.DOWNVOTE,
            })}
          />
        </Button>
      </div>
      <div className="flex flex-col gap-2 pl-2">
        <Button
          variant="link"
          onClick={openFile}
          className="pl-0 justify-start h-auto flex items-center gap-4"
        >
          <File className="w-6 h-6" />
          <h4>{material.name}</h4>
        </Button>
        {showCommentsLink && (
          <Link to={`./material/${material.id}`} className="">
            <Button variant="ghost" className="pl-0  flex items-center p-1 h-auto text-sm gap-2">
              <MessageSquareText className="w-5 h-5" />
              Comments
            </Button>
          </Link>
        )}
        <div className="flex gap-1 flex-wrap">
          {material.tags.map((tag) => (
            <Badge key={tag.id}>{tag.name}</Badge>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MaterialItem;
