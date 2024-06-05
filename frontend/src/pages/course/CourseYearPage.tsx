import { Card, CardContent, CardDescription } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import { File } from "lucide-react";
import { useParams } from "react-router-dom";
import { MaterialGroupService } from "@/api/material-group.service.ts";
import { keepPreviousData } from "@tanstack/react-query";
import { MaterialGroup } from "@/types/MaterialGroup.ts";
import { Material } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion.tsx";
import useLocalStorage from "@/hooks/useLocalStorage.ts";
import { useMemo } from "react";
import Loading from "@/components/Loading.tsx";

const CourseYearPage = () => {
  const { courseYearId } = useParams<{ courseYearId: string }>();
  const defaultValue = useMemo(() => ({}), []);
  const [openValues, setOpenValues] = useLocalStorage<Record<string, Array<string>>>(
    "open-group-values",
    defaultValue,
  );

  const currentOpenValues = useMemo(() => {
    if (!openValues) {
      return [];
    }
    if (!openValues[courseYearId!]) {
      return [];
    }
    return openValues[courseYearId!];
  }, [courseYearId, openValues]);

  const updateValues = (values: Array<string>) => {
    setOpenValues({
      ...openValues,
      [courseYearId!]: values,
    });
  };

  const groupQuery = MaterialGroupService.useGetAll(
    {
      courseYearId,
      size: 100,
    },
    {
      placeholderData: keepPreviousData,
    },
  );

  return (
    <Card className="w-full md:px-10 border-none space-y-4 relative">
      <CardContent className="flex flex-col gap-8 p-0">
        <Accordion type="multiple" value={currentOpenValues} onValueChange={updateValues}>
          {groupQuery.isLoading ? (
            <Loading />
          ) : !groupQuery.data?.content.length ? (
            <span>No materials available yet</span>
          ) : null}
          {groupQuery.data?.content.map((group) => (
            <MaterialGroupCard key={group.id} group={group} />
          ))}
        </Accordion>
      </CardContent>
    </Card>
  );
};

interface MaterialGroupCardProps {
  group: MaterialGroup;
}

const MaterialGroupCard = ({ group }: MaterialGroupCardProps) => {
  return (
    <AccordionItem value={group.id.toString()}>
      <AccordionTrigger>
        <h3 className="text-2xl">{group.name}</h3>
      </AccordionTrigger>
      <AccordionContent>
        {group.description && <CardDescription>{group.description}</CardDescription>}
        <div>
          {group.materials?.map((material) => (
            <MaterialItem key={material.id} material={material} />
          ))}
        </div>
      </AccordionContent>
    </AccordionItem>
  );
};

interface MaterialItemProps {
  material: Material;
}

const MaterialItem = ({ material }: MaterialItemProps) => {
  const { mutate: getFile } = MaterialService.useGetFile();
  const openFile = () => {
    const newWindow = window.open();
    getFile(material.id, {
      onSuccess: (data) => {
        const url = window.URL.createObjectURL(data);
        if (newWindow) newWindow.location.href = url;
      },
    });
  };
  return (
    <div className="flex items-center gap-2 p-4 border rounded-md my-2">
      <Button variant="link" onClick={openFile} className="p-2 h-auto flex items-center gap-4">
        <File className="w-6 h-6" />
        <h4>{material.name}</h4>
      </Button>
    </div>
  );
};

export default CourseYearPage;
